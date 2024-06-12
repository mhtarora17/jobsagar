package com.job.sagar.config;

import com.job.sagar.shutdown.GracefulShutdownTomcatConnectorCustomizer;
import com.job.sagar.shutdown.GracefulShutdownTomcatContainerCustomizer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class GracefulShutdownAutoConfig {

@Bean
public GracefulShutdownTomcatContainerCustomizer gracefulShutdownTomcatContainerCustomizer(
        GracefulShutdownTomcatConnectorCustomizer connectorCustomizer) {
    return new GracefulShutdownTomcatContainerCustomizer(connectorCustomizer);
}
@Bean
public GracefulShutdownTomcatConnectorCustomizer gracefulShutdownTomcatConnectorCustomizer(
                ApplicationContext ctx) {

    return new GracefulShutdownTomcatConnectorCustomizer(ctx);
    }
}