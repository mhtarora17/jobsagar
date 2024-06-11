package com.job.sagar.exception;

import com.google.common.collect.Iterables;
import com.job.sagar.Utils.CommonUtils;
import com.job.sagar.Utils.LocalizationErrorCachingUtility;
import com.job.sagar.Utils.Pair;
import com.job.sagar.dao.IServiceErrorDao;
import com.job.sagar.datadog.MetricsAgent;
import com.job.sagar.model.ServiceError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.job.sagar.constant.Constants.*;
import static com.job.sagar.constant.ErrorCodesConstant.INTERNAL_SERVER_ERROR_CODE;

@Component
@Qualifier("serviceErrorFactory")
@DependsOn({"localizationErrorCachingUtility"})
public class ServiceErrorFactory implements ApplicationContextAware {
    private static final Logger LOGGER = LogManager.getLogger(ServiceErrorFactory.class);
    private static Map<Pair<String, String>, ServiceError> SERVICE_ERROR_MAP = new ConcurrentHashMap<>();
    private static Map<Pair<String, String>, ServiceError> SERVICE_ERROR_MAP_RESPONSE_CODE = new ConcurrentHashMap<>();
    private IServiceErrorDao serviceErrorDao;
    private ApplicationContext applicationContext;
    private static LocalizationErrorCachingUtility localizationErrorCachingUtility;
    private static MetricsAgent metricsAgent;

    @Autowired
    public ServiceErrorFactory(IServiceErrorDao serviceErrorDao,
                               ApplicationContext applicationContext, LocalizationErrorCachingUtility localizationErrorCachingUtility1, MetricsAgent metricsAgent1) {
        this.serviceErrorDao =serviceErrorDao;
        this.applicationContext =applicationContext;
        localizationErrorCachingUtility =localizationErrorCachingUtility1;
        metricsAgent=metricsAgent1;
    }

    public static Optional<BaseException> getException(String serviceName, String errorCode) {
        ServiceError serviceError = SERVICE_ERROR_MAP.get(createKey(serviceName, errorCode));
        if (serviceError == null)
            return Optional.empty();
        try {
            Class exceptionClass = Class.forName (serviceError.getExceptionName());
            BaseException e = (BaseException) exceptionClass.getConstructor(String.class, int.class, String.class)
                    .newInstance(serviceError.getStatus(), serviceError.getResponseCode(),
                            serviceError.getErrorMessage());
            if (ThreadContext.containsKey(LOCALE)) {
                String locale = ThreadContext.get(LOCALE);
                String serviceErrorKey = "TSE-" + serviceName+"-"+errorCode;
                String errorMessage = localizationErrorCachingUtility.getErrorMessageForKeyAndLocale(serviceErrorKey, locale);
                if (errorMessage != null && errorMessage.length() != 0 && !("null".equalsIgnoreCase (errorMessage))) {
                    e.setCustomMessage (errorMessage);
                }
            }
            if (serviceError != null) {
                return Optional.ofNullable(e);
            } else {
                return Optional.empty();
            }
        }catch (ClassNotFoundException ce) {
            LOGGER.error("[ServiceErrorFactory] No class found for exception name {}", CommonUtils.exceptionFormatter (ce));
        } catch (NoSuchMethodException ne) {
            LOGGER.error("[ServiceErrorFactory] Check if exception constructor is defined properly {}", CommonUtils.exceptionFormatter(ne));
        } catch (IllegalAccessException ie) {
            LOGGER.error("[ServiceErrorFactory] Check if constructor can be accessed {}", CommonUtils.exceptionFormatter (ie));
        } catch (InstantiationException ie) {
            LOGGER.error("[ServiceErrorFactory] Exception cannot be initialized {}", CommonUtils.exceptionFormatter (ie));
        } catch (InvocationTargetException ie) {
            LOGGER.error("[ServiceErrorFactory] Error while initializing exceptions {}", CommonUtils.exceptionFormatter(ie));
        } catch (Exception e) {
            LOGGER.error("[ServiceErrorFactory] Error while initializing exceptions {}", CommonUtils.exceptionFormatter(e));
        }
        return Optional.empty();
}

public static BaseException getExceptionV2 (String serviceName, String errorCode) {
    Optional<BaseException> optional = ServiceErrorFactory.getException(serviceName, errorCode);
    if (optional.isPresent()) {
        return optional.get();
    } else {
        return new BaseException(FAILED, INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR);
    }
}
public static BaseException getExceptionV3 (Exception ex, String flow, String serviceName, String errorCode, Map<String, String> properties) {
        pushDBFailureEvent(ex, flow, properties);
        Optional<BaseException> optional = ServiceErrorFactory.getException (serviceName, errorCode);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return new BaseException (FAILED, INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR);
        }
    }


public static void pushDBFailureEvent (Exception ex, String flow, Map<String, String> properties) {
    if (CommonUtils.checkIfDBFailure(ex)) {
        metricsAgent.pushDBFailureMetric(flow, ex.getClass().getName());
    }
}
public static Optional<Integer> getResponseCode (String serviceName, String errorCode) {
        ServiceError serviceError = SERVICE_ERROR_MAP.get(createKey(serviceName, errorCode));
        if (serviceError != null && serviceError.getResponseCode() != null) {
            return Optional.of(serviceError.getResponseCode());
        } else {
            return Optional.empty();
        }
    }
public static Optional<String> getStatus (String serviceName, String errorCode) {
    ServiceError serviceError = SERVICE_ERROR_MAP.get(createKey(serviceName, errorCode));
    if (serviceError != null) {
        return Optional.of(serviceError.getStatus());
    } else {
        return Optional.empty();
    }
}

public static Optional<String> getErrorMessage(String serviceName, String errorCode) {
    ServiceError serviceError = SERVICE_ERROR_MAP.get(createKey(serviceName, errorCode));
    if (serviceError != null && serviceError.getDescription() != null) {
        return Optional.of(serviceError.getDescription());
    } else {
        return Optional.empty();
    }
}
public static Optional<ServiceError> getServiceErrorEntity(String serviceName, String errorCode) {
    ServiceError serviceError = SERVICE_ERROR_MAP.get(createKey(serviceName, errorCode));
    if (serviceError != null) {
        return Optional.of(serviceError);
    } else {
        return Optional.empty();
    }
}

public static Optional<String> getErrorMessageRespCode (String serviceName, String responseCode) {
    ServiceError serviceError = SERVICE_ERROR_MAP_RESPONSE_CODE.get(createKey(serviceName, responseCode));
    if (serviceError != null && serviceError.getErrorMessage() != null) {
        return Optional.ofNullable(serviceError.getErrorMessage());
    } else {
        return Optional.empty();
    }
}
public static Optional<Map<Pair<String, String>, ServiceError>> getErrorResponseMap() {
        Map<Pair<String, String>, ServiceError> errorResponseMap = new HashMap<>();
        errorResponseMap.putAll(SERVICE_ERROR_MAP_RESPONSE_CODE);
        if (errorResponseMap.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(errorResponseMap);
        }
    }
private static Pair<String, String> createKey(String serviceName, String errorCode) {
    return new Pair<>(serviceName, errorCode);
}
@PostConstruct
public void initializeModule() {
   Iterable<ServiceError> serviceErrors = serviceErrorDao.getAllServiceErrors();
   reloadServiceError(serviceErrors);
}

public static void reloadServiceError (Iterable<ServiceError> serviceErrors) {
    LOGGER.info("Cache data size: {}", Iterables.size(serviceErrors));
    for (ServiceError serviceError : serviceErrors) {
        Pair<String, String> key = createKey(serviceError.getServiceName(),
                serviceError.getErrorCode());
        SERVICE_ERROR_MAP.put(key, serviceError);
        Pair<String, String> keyRespCode = createKey(serviceError.getServiceName(),
                String.valueOf(serviceError.getResponseCode()));
        SERVICE_ERROR_MAP_RESPONSE_CODE.put(keyRespCode, serviceError);
    }
    LOGGER.info("DB data successfully cached.");
}
@Override
@Autowired
public void setApplicationContext (ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
}

}
