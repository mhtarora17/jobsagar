package com.job.sagar.config;

import com.job.sagar.datadog.MetricsAgent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.job.sagar.constant.ExecutorConstants.*;

@Log4j2
@Configuration
@PropertySource("classpath: threadpool.properties")
public class JobSagarExecutorConfig {

    private Environment env;

    private int getProperty(String key) {
        return env.getProperty(key, Integer.class, 5);
    }

    @Autowired
    private MetricsAgent metricsAgent;

    @Bean
    @Qualifier("asyncExecutor")
    public TaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(getProperty(ASYNC_EXECUTOR_CORE_POOL_SIZE));
        executor.setMaxPoolSize(getProperty(ASYNC_EXECUTOR_MAX_POOL_SIZE));
        executor.setQueueCapacity(getProperty(ASYNC_EXECUTOR_QUEUE_CAPACITY));
        executor.setThreadNamePrefix(ASYNC_EXECUTOR_THREAD_NAME);
        executor.setKeepAliveSeconds(getProperty(ASYNC_EXECUTOR_TIMEOUT));
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        });
        executor.initialize();
        return executor;
    }

    @Bean
    @Qualifier(DB_FAILURE_METRIC_EXECUTOR)
    public TaskExecutor dbFailureMetricExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(getProperty(DB_FAILURE_METRIC_CORE_POOL_SIZE));
        executor.setMaxPoolSize(getProperty(DB_FAILURE_METRIC_MAX_POOL_SIZE));
        executor.setQueueCapacity(getProperty(DB_FAILURE_METRIC_QUEUE_CAPACITY));
        executor.setThreadNamePrefix(DB_FAILURE_METRIC_THREAD_NAME);
        executor.setKeepAliveSeconds(getProperty(DB_FAILURE_METRIC_TIMEOUT));
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        });
        executor.initialize();
        return executor;
    }
}