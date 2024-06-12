package com.job.sagar.shutdown;

import com.job.sagar.dao.impl.ServiceErrorDaoImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.Connector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


public class GracefulShutdownTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

    private static final Logger logger = LogManager.getLogger(GracefulShutdownTomcatConnectorCustomizer.class);
    private static final int GRACE_PERIOD = 29;
    private final ApplicationContext applicationContext;
    private Connector connector;

    public GracefulShutdownTomcatConnectorCustomizer(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @EventListener(ContextClosedEvent.class)
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public void contextClosed(ContextClosedEvent event) throws InterruptedException {
        if (connector == null) {
            return;
        }
        if (isEventFromLocalContext(event)) {
            stopAcceptingNewRequests();
            shutdownThreadPoolExecutor();
        }
    }

    private void stopAcceptingNewRequests() {
        connector.pause();
        logger.info("Paused {} to stop accepting new requests", connector);
    }

    private void shutdownThreadPoolExecutor() throws InterruptedException {
        ThreadPoolExecutor executor = getThreadPoolExecutor();
        if (executor != null) {
            executor.shutdown();
            awaitTermination(executor);
        }
    }

    private void awaitTermination(ThreadPoolExecutor executor) throws InterruptedException {
        if (executor.awaitTermination(GRACE_PERIOD, TimeUnit.SECONDS)) {
            return;
        }
        logMessageIfThereAreStillActiveThreads(executor);
    }

    private void logMessageIfThereAreStillActiveThreads (ThreadPoolExecutor executor) {
        if (executor.getActiveCount() > 0) {
            logger.warn("{} thread(s) still active, force shutdown", executor.getActiveCount());
        }
    }
    private ThreadPoolExecutor getThreadPoolExecutor () {
        Executor executor = connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            return (ThreadPoolExecutor) executor;
        }
        return null;
    }

    private boolean isEventFromLocalContext (ContextClosedEvent event) {
        return event.getApplicationContext().equals(applicationContext);
    }


}


