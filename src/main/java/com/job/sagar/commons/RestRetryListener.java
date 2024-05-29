package com.job.sagar.commons;


import com.job.Utils.CommonsUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

public class RestRetryListener extends RetryListenerSupport {
    
    private static final Logger log = LogManager.getLogger(RestRetryListener.class);

    public RestRetryListener() {
    }


    public <T, E extends Throwable> void onError (RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        try {
            log.error("Retryable method threw {}th exception {}", context.getRetryCount(), throwable.toString());
        } catch (Exception var5) {
            log.error( "Exception occurred onError in retry {}", CommonsUtility.exceptionFormatter (var5));
        }
    }
}
