package com.job.sagar.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class UtilityConfig {


    static {
        new ClassLoaderMetrics().bindTo(Metrics.globalRegistry);
        new JvmMemoryMetrics().bindTo(Metrics.globalRegistry);
        new JvmGcMetrics().bindTo(Metrics.globalRegistry);
        new ProcessorMetrics().bindTo(Metrics.globalRegistry);
        new JvmThreadMetrics().bindTo(Metrics.globalRegistry);
        new JvmHeapPressureMetrics().bindTo(Metrics.globalRegistry);
        new FileDescriptorMetrics().bindTo(Metrics.globalRegistry);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}

