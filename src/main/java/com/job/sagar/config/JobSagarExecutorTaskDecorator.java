package com.job.sagar.config;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class JobSagarExecutorTaskDecorator  implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = ThreadContext.getContext();
        return () -> {
            try {
                setContext(contextMap);
                runnable.run();
            } finally {
                ThreadContext.clearMap();
                ;
            }
        };
    }

    private void setContext(Map<String, String> storedContext) {
        if (storedContext == null) {
            ThreadContext.clearMap();
        } else {
            ThreadContext.clearMap();
            ThreadContext.putAll(storedContext);
        }
    }
}