package com.job.sagar.localization.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.sagar.Utils.CommonsUtility;
import com.job.sagar.Utils.LocalizationErrorCachingUtility;
import com.job.sagar.Utils.MetricsUtility;
import com.job.sagar.Utils.RestMethod;
import com.job.sagar.exception.BadRequestException;
import com.job.sagar.localization.LocalizationClient;
import com.job.sagar.response.LocaleResponse;
import com.job.sagar.response.LocalizationErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

import static com.job.sagar.constant.Constants.*;
import static com.job.sagar.constant.EndPointConstants.LOCALIZATION_V1_GET_LANGUAGES;
import static com.job.sagar.constant.MetricsConstants.LOCALIZATION_GET_API;

@Component
@Log4j2
public class LocalizationClientImpl implements LocalizationClient {

    private static final Logger log = LogManager.getLogger(LocalizationClientImpl.class);

    @Value("${localization-service-http-url}")
    private String localizationIp;

    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public LocalizationErrorResponse getErrorMessages (Integer pageNum, Integer limit, String locale, String timestamp) throws Exception {
        log.info("Request receive to get error message list from localization system");
        try {
            HttpHeaders httpHeaders = createHttpHeaders();
            final String url = UriComponentsBuilder.fromHttpUrl(localizationIp).path("/localisation/v1/getMessages")
                    .queryParam(SERVICE, JOB_SAGAR)
                    .queryParam("language", locale)
                    .queryParam("timestamp", timestamp)
                    .queryParam("page", pageNum).queryParam("pageSize", limit)
                .build()
                .toUriString();
        log.debug("request for error message url = {} and timestamp {} and page No. {} and limit {}", url, timestamp, pageNum, limit);
        MetricsUtility.setDataForGetApi("/localisation/v1/getMessages","localisation_get_api");
        String localizationErrorResponseStr = RestMethod
                        .restRequest(httpHeaders, String.class, url, LOCALIZATION_SERVICE, HttpMethod. GET, null);
        log.info( "Response received from localization service {}", localizationErrorResponseStr);
        Map<String, Object> localizationErrorResponseMap = objectMapper.readValue (localizationErrorResponseStr, new TypeReference<Map<String, Object>>(){});
                if (localizationErrorResponseMap != null && (localizationErrorResponseMap.containsKey("ERROR") || localizationErrorResponseMap.containsKey("error")))
                {
                    log.info("Error response received for localization: ()", localizationErrorResponseMap);
                    throw new BadRequestException("failure", 9999, "Invalid request");
                }
                LocalizationErrorResponse localizationErrorResponse = new LocalizationErrorResponse();
                localizationErrorResponse.setResponse(localizationErrorResponseMap);
                return localizationErrorResponse;
            } catch (Exception ex) {
                log.error("Exception occurred while calling localization service {}",
                        CommonsUtility.exceptionFormatter(ex));
                throw ex;
            }
    }

    public LocaleResponse getLocaleList() throws Exception {
        log.info("Request receive to get locale list from localization system");
        try {
            HttpHeaders httpHeaders = createHttpHeaders();
            final String url = UriComponentsBuilder.fromHttpUrl(localizationIp).path(LOCALIZATION_V1_GET_LANGUAGES)
                    .queryParam(SERVICE, JOB_SAGAR)
                    .build()
                    .toUriString();
            log.debug("request for error message url = {}", url);
            MetricsUtility.setDataForGetApi(LOCALIZATION_V1_GET_LANGUAGES, LOCALIZATION_GET_API);
            String localeResponseStr = RestMethod.restRequest(httpHeaders, String.class, url, LOCALIZATION_SERVICE, HttpMethod.GET, null);
            log.info("Response received from localization service {}", localeResponseStr);
            Map<String, String> responseMap = objectMapper.readValue(localeResponseStr, new TypeReference<Map<String, String>>() {
            });
            if (responseMap != null && (responseMap.containsKey("ERROR") || responseMap.containsKey("error"))) {
                log.info("Error response received for localization: {}", responseMap);
                throw new BadRequestException("failure", 9999, "Invalid request");
            }
            LocaleResponse localeResponse = new LocaleResponse();
            localeResponse.setResponse(responseMap);
            return localeResponse;
        } catch (Exception ex) {
            log.error("Exception occurred while calling localization service for fetching locale list: {}",
                    CommonsUtility.exceptionFormatter(ex));
            throw ex;
        }
    }
    private HttpHeaders createHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}