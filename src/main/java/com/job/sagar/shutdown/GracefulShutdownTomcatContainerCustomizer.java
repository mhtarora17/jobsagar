package com.job.sagar.shutdown;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

public class GracefulShutdownTomcatContainerCustomizer
        implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    private final GracefulShutdownTomcatConnectorCustomizer connectorCustomizer;

    public GracefulShutdownTomcatContainerCustomizer(GracefulShutdownTomcatConnectorCustomizer connectorCustomizer) {
        this.connectorCustomizer = connectorCustomizer;
    }

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        if (factory instanceof TomcatServletWebServerFactory) {
            customizeTomcat((TomcatServletWebServerFactory) factory);
        }
    }

    private void customizeTomcat(
            TomcatServletWebServerFactory tomcatServletWebServerFactory) {
        TomcatServletWebServerFactory factory = tomcatServletWebServerFactory;
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connectorCustomizer);
    }
}
