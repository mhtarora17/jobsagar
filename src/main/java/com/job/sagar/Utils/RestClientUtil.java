package com.job.sagar.Utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import com.job.sagar.commons.RestRetryListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.config. RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn. PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.util.Args;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter. FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@PropertySource(
value = {"classpath:http-client-${spring.profiles.active}.properties"},
ignoreResourceNotFound = true
)
public class RestClientUtil {

private static final Logger log = LogManager.getLogger(RestClientUtil.class);

@Value("${connection.request.timeout:1000}")
private Integer connectionRequestTimeout;

@Value("${connection.timeout:5000}")
private Integer connectionTimeout;

@Value("${read.timeout:50000}")
private Integer readTimeout;

@Value("${max.total.connection:50}")
private Integer maxTotalConnection;

@Value("${max.per.channel:50}")
private Integer maxPerChannel;

private List<String> publicServices;

@Autowired
private Environment httpClientProperties;

private static ConcurrentHashMap<String, RestTemplate> cache = new ConcurrentHashMap();

private static List<HttpMessageConverter<?>> messageConverters = new ArrayList();

private static final Map<Class<? extends Throwable>, Boolean> RETRY_EXCEPTIONS = new HashMap();

public RestClientUtil() {
}

@PostConstruct
public void init() {
    
    messageConverters.add(new FormHttpMessageConverter());
    this.populatePublicServices();
}

private void populatePublicServices() {
    String publicServiceInProperties = this.httpClientProperties.getProperty("public.service.list");
    this.publicServices = (List) (StringUtils.isBlank(publicServiceInProperties) ? new ArrayList() : Arrays.asList(publicServiceInProperties.split(",")));
}

private Integer getConnectionTimeout(String serviceName) {
    String value = this.httpClientProperties.getProperty(serviceName + ".connection.timeout");
    if (StringUtils.isNotEmpty (value)) {
        try {
            return Integer.parseInt(value);
        } catch (Exception var4) {
        }
    }
    return this.connectionTimeout;
}

private Integer getConnectionRequestTimeout(String serviceName) {
    String value = this.httpClientProperties.getProperty(serviceName + ".connection.request.timeout");
    if (StringUtils.isNotEmpty(value)) {
        try {
            return Integer.parseInt(value);
        } catch (Exception var4) {
        }
    }
    return this.connectionRequestTimeout;
}

private Integer getReadTimeout(String serviceName) {
    String value = this.httpClientProperties.getProperty(serviceName + ".read.timeout");
        if (StringUtils.isNotEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception var4) {
            }
        }
            return this.readTimeout;
        }
private Integer getMaxTotalConnection (String serviceName) {
    String value = this. httpClientProperties.getProperty(serviceName + " .max.total.connection");
        if (StringUtils.isNotEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception var4) {
            }
        }
        return this.maxTotalConnection;
    }
private Integer getMaxPerChannel (String serviceName) {
    String value = this.httpClientProperties.getProperty(serviceName + "max.per.channel");
        if (StringUtils.isNotEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception var4) {
            }
        }    
        return this.maxPerChannel;
    }


private Integer getConnectionIdleTime (String serviceName) {
    String value = this.httpClientProperties.getProperty(serviceName + "connection.idle.time.ms");
        if (StringUtils.isNotEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception var4) {
            }
        }
        return Integer.MIN_VALUE;
}
   
private Integer getIdleConnectionValidationTime (String serviceName) {
    String value = this. httpClientProperties.getProperty(serviceName + ".connection.idle.validation.time.ms");
        if (StringUtils.isNotEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception var4) {
            }
        }
        return Integer.MIN_VALUE;
    }

private Integer getDefaultConnectionKeepAliveTime (String serviceName) {
    String value = this.httpClientProperties.getProperty(serviceName + ".connection.keep.alive.default.time.ms");
        if (StringUtils.isNotEmpty (value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception var4) {
            }
        }
        return Integer.MIN_VALUE;
    }

private Integer getProxyPortNumber() {
    String value = this. httpClientProperties.getProperty("public.proxy.port.number");
    if (StringUtils.isNotEmpty(value)) {
        try {
            return Integer.parseInt(value);
        } catch (Exception var3) {
        }
    }
    return null;
}

private String getProxyIpAddress() {
    String value = this. httpClientProperties.getProperty("public.proxy.ip.address");
    if (StringUtils.isNotEmpty(value)) {
        try {
            return value;
        } catch (Exception var3) {
        }
    }    
    return null;
}

private ClientHttpRequestFactory createRequestFactory(String serviceName) {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(this.getMaxTotalConnection (serviceName));
    connectionManager.setDefaultMaxPerRoute(this.getMaxPerChannel(serviceName));
    int idleConnectionValidationTimeMs = this.getIdleConnectionValidationTime (serviceName);
    if (idleConnectionValidationTimeMs > 0) {
        connectionManager.setValidateAfterInactivity (idleConnectionValidationTimeMs);
    }
    RequestConfig config = RequestConfig.custom().setConnectTimeout(this.getConnectionTimeout(serviceName)).setConnectionRequestTimeout(this.getConnectionRequestTimeout(serviceName)).setSocketTimeout(this.getReadTimeout(serviceName)).build();
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setConnectionManager(connectionManager).setDefaultRequestConfig(config);
    boolean isPublicService = this.publicServices.stream().anyMatch(serviceName::equalsIgnoreCase);
    if (isPublicService) {  
        String proxyIpAddress = this.getProxyIpAddress();
        Integer proxyPortNumber = this.getProxyPortNumber();
        if (proxyIpAddress != null && proxyPortNumber != null) { 
            httpClientBuilder.setProxy (new HttpHost(proxyIpAddress, proxyPortNumber));
        }
    }
    int connectionIdleTimeMs = this.getConnectionIdleTime (serviceName);
    if (connectionIdleTimeMs > 0) {
        httpClientBuilder.setConnectionTimeToLive(connectionIdleTimeMs, TimeUnit.MILLISECONDS);
    }
    int defaultConnectionKeepAliveTime = this.getDefaultConnectionKeepAliveTime (serviceName);
    if (defaultConnectionKeepAliveTime > 0) {
        httpClientBuilder.setKeepAliveStrategy (this.getCustomkeepAliveStrategy(defaultConnectionKeepAliveTime));
    }
    CloseableHttpClient httpClient = httpClientBuilder.build();
    return new HttpComponentsClientHttpRequestFactory(httpClient);
}

private ConnectionKeepAliveStrategy getCustomkeepAliveStrategy(int defaultConnectionKeepAliveTime) {
    return (response, context) -> {
        Args.notNull(response, "HTTP response");
        HeaderElementIterator it = new BasicHeaderElementIterator (response.headerIterator("Keep-Alive"));
        String param;
        String value;
        do {
            if (it.hasNext()) {
                return (long)defaultConnectionKeepAliveTime;
            }
        HeaderElement he = it.nextElement();
        param = he.getName();
        value = he.getValue();
    } while(value == null || !param.equalsIgnoreCase( "timeout"));
        return Long.parseLong (value) * 1000L;
    };
}

private RestTemplate createRestTemplate(ClientHttpRequestFactory factory) {
    RestTemplate restTemplate = new RestTemplate(factory);
    restTemplate.getMessageConverters().addAll(messageConverters);
    Iterator var3 = restTemplate.getMessageConverters().iterator();
    while (var3.hasNext()) {
        HttpMessageConverter messageConverter = (HttpMessageConverter) var3.next();
        if (messageConverter instanceof AbstractJackson2HttpMessageConverter) {
            ((AbstractJackson2HttpMessageConverter) messageConverter).getObjectMapper().configure (DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        }
    }
    return restTemplate;
}

public RetryTemplate restRetryTemplate(int maxAttempt, long retryIntervalMs, double multiplier) {
    RetryTemplate retryTemplate = new RetryTemplate();
    ExponentialBackOffPolicy backoffPolicy = new ExponentialBackOffPolicy();
    backoffPolicy.setInitialInterval(retryIntervalMs);
    backoffPolicy.setMultiplier(multiplier);
    retryTemplate.setBackOffPolicy (backoffPolicy);
    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempt, RETRY_EXCEPTIONS, true, false);
    retryTemplate.setRetryPolicy (retryPolicy);
    retryTemplate.registerListener(new RestRetryListener());
    return retryTemplate;
}

public RestTemplate getRestTemplate(String serviceName) {
    if (cache.get(serviceName) == null) {
        ClientHttpRequestFactory factory = this.createRequestFactory (serviceName);
        RestTemplate template = this.createRestTemplate(factory);
        cache.putIfAbsent (serviceName, template);
    }
    return (RestTemplate) cache.get(serviceName);
}

public <T> ResponseEntity<T> customExchange (RestTemplate restTemplate, RetryTemplate retryTemplate, String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {

    try {
    return (ResponseEntity)retryTemplate.execute((arg) -> {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    });
    } catch (RestClientException var9) {
        log.error("Exception occurred in rest exchange {}", CommonsUtility.exceptionFormatter(var9));
        if (var9 instanceof ResourceAccessException && var9.getCause() instanceof ConnectException) {
            this.invalidateDnsCache();
            return (ResponseEntity)retryTemplate.execute((arg) -> {
                return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
            });
        } else {
            throw var9;
        }
    }   

}

public void invalidateDnsCache() {
    this.invalidateCache("addressCache");
    this.invalidateCache("negativeCache");
}

private void invalidateCache (String cacheName) {
    try {
    log.info("Invalidating dns cache {}", cacheName);
    Class<InetAddress> klass = InetAddress.class;
    Field acf = klass.getDeclaredField(cacheName);
    acf.setAccessible(true);
    Object addressCache = acf.get((Object)null);
    Class cacheklass = addressCache.getClass();
    Field cf = cacheklass.getDeclaredField("cache");
    cf.setAccessible (true);
    Map<String, Object> cache = (Map) cf.get(addressCache);
    cache.clear();
    } catch (Exception var8) {
        log.error("Exception {} occurred while invalidating dns cache", CommonsUtility.exceptionFormatter (var8));
    }
}

static {
    RETRY_EXCEPTIONS.put(UnknownHostException.class, true);
}   

}