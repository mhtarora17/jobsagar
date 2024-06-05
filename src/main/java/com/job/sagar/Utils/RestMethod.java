package com.job.sagar.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.sagar.datadog.MetricsAgent;
import com.job.sagar.exception.BaseException;
import com.job.sagar.exception.MicroServiceUnavailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.job.sagar.constant.Constants.*;
import static com.job.sagar.constant.Constants.INTERNAL_SERVER_ERROR;
import static com.job.sagar.constant.ErrorCodesConstant.*;

@Component
public class RestMethod {
    private static final Logger logger = LogManager.getLogger(RestMethod.class);
    private static RestClientUtil restClientUtil;
    private static MetricsAgent metricsAgent;
    private static ObjectMapper objectMapper;
    private static final Map<String, Integer> UNAVAILABLE_ERROR_CODE_MAP = new HashMap<>();
    private static final HashSet<String> RETRY_ENABLED_SERVICES = new HashSet<>(Arrays.asList());

    static {
    }

    @Autowired
    RestMethod(RestClientUtil restclientUtil, MetricsAgent metricsAgent, ObjectMapper objectMapper) {
        RestMethod.restClientUtil = restclientUtil;
        RestMethod.metricsAgent = metricsAgent;
        RestMethod.objectMapper = objectMapper;
    }

    public static <T, U> U restRequest(final HttpHeaders httpHeaders, final Class<U> responseClass,
                                       final String url, final String serviceName, final HttpMethod httpMethod,
                                       final T requestObject) {
        try {
            return getResponse(httpHeaders, responseClass, url, serviceName, httpMethod, requestObject);
        } catch (BaseException ex) {
            if (ex.getCode() == INTEGRATION_SERVICE_UNAVAILABLE_ERROR_CODE && RETRY_ENABLED_SERVICES.contains(serviceName) && httpMethod.toString().equals(HttpMethod.GET.toString())) {
                logger.debug("Retrying hitting url:{}.", url);
                return getResponse(httpHeaders, responseClass, url, serviceName, httpMethod, requestObject);
            } else if (ex.getCode() == INTEGRATION_SERVICE_UNAVAILABLE_ERROR_CODE && RETRY_ENABLED_SERVICES.contains(serviceName)) {
                logger.debug("Retrying hitting url:{},", url);
                return getResponse(httpHeaders, responseClass, url, serviceName, httpMethod, requestObject);
            }
            throw ex;
        }
    }

    private static <T, U> U getResponse(final HttpHeaders httpHeaders, final Class<U> responseClass,
                                        final String url, final String serviceName, final HttpMethod httpMethod,
                                        final T requestObject) {
        RestTemplate restTemplate = restClientUtil.getRestTemplate(serviceName);
        try {
            HttpEntity httpEntity = new HttpEntity(requestObject, httpHeaders);
            ResponseEntity<U> responseEntity =
                    restTemplate.exchange(url, httpMethod, httpEntity, responseClass);
            return responseEntity.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Http Status Code error while calling external service. {}, Response Body: {}."
                    , CommonUtils.exceptionFormatter(e), e.getResponseBodyAsString());
            throw new BaseException (FAILED, INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR);
        }
        catch (ResourceAccessException e) {
            logger.error("Micro Service unavailable error while calling external service. {}",
                CommonUtils.exceptionFormatter(e));
        pushUnavailableCodeToDatadog(serviceName);
        throw new MicroServiceUnavailableException(PENDING,
                    INTEGRATION_SERVICE_UNAVAILABLE_ERROR_CODE,
                    INTERNAL_SERVER_ERROR);
        }
    }
    private static boolean checkForResponseBodyInHttpStatusCodeException(String url, String serviceName, HttpStatusCodeException e) {
        try {
            return (url.contains("/nach/nameMatch") && Arrays.asList(400, 404, 412)
                    .contains(e.getStatusCode().value())) || (url.contains("int/initiate/recovery") && (480 == e.getStatusCode().value()));
        } catch (Exception ex) {
            return false;
        }
    }
    private static void pushUnavailableCodeToDatadog (String serviceName) {
        try {
        int code = UNAVAILABLE_ERROR_CODE_MAP.getOrDefault(serviceName, UNKNOWN_SERVICE_UNAVAILABLE_ERROR_CODE);
//        metricsAgent.recordResponseCodeCount("200", String.valueOf(code), ThreadContext.get("client"));
        } catch (Exception e) {
            logger.error("Exception: {} occurred while sending unavailable code to Datadog.", CommonUtils.exceptionFormatter (e));
        }
    }
}



