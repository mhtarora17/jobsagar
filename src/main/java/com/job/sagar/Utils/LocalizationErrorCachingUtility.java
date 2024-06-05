package com.job.sagar.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.sagar.dao.SchedulerServiceDao;
import com.job.sagar.dao.impl.SchedulerServiceDaoImpl;
import com.job.sagar.exception.BadRequestException;
import com.job.sagar.localization.LocalizationClient;
import com.job.sagar.model.SchedulerData;
import com.job.sagar.response.LocaleResponse;
import com.job.sagar.response.LocalizationErrorResponse;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static com.job.sagar.constant.Constants.*;

@Log4j2
@Component
public class LocalizationErrorCachingUtility {

    private static final Logger log = LogManager.getLogger(LocalizationErrorCachingUtility.class);

    private static final String LOCALIZATION_LOCALE_CACHING_SCHEDULER = "localizationLocaleCachingScheduler";
    private static final String LOCALIZATION_ERROR_CACHING_SCHEDULER = "localizationErrorCachingScheduler";
    @Autowired
    private LocalizationClient localizationClient;

    @Autowired
    private SchedulerServiceDao schedulerServiceDao;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Map<String, String>> errorMessagesCache;

    private List<String> localeCache;

    private static final TypeReference<List<Map<String, String>>> TYPE_REFERENCE = new TypeReference<List<Map<String, String>>>() {};

    @Scheduled(initialDelayString = "${locale.cache.scheduler.initial.delay.ms}", fixedDelayString = "${locale.cache.scheduler.fixed.delay.ms}")
    @SchedulerLock(name = LOCALIZATION_LOCALE_CACHING_SCHEDULER, lockAtMostFor = "PT40M")
    public void fetchLocales() {
        ThreadContext.put(REQUEST_ID, "Locale-cache-scheduler" + Instant.now().toEpochMilli() / 1000);
        try {
            log.info("scheduler started localization LocaleCachingScheduler at {}", LocalDateTime.now());
            this.setLocaleCache();
        } catch (Exception ex) {
            log.error("Exception occurred while updating locale list: {}", CommonsUtility.exceptionFormatter(ex));
        } finally {
            log.info("scheduler completed localization LocaleCachingScheduler at {}", LocalDateTime.now());
            ThreadContext.clearMap();
        }
    }

    @Scheduled(initialDelayString = "${locale.error.cache.scheduler.initial.delay.ms}", fixedDelayString = "${locale.error.cache.scheduler.fixed.delay.ms}")
    @SchedulerLock(name = LOCALIZATION_ERROR_CACHING_SCHEDULER, lockAtMostFor = "PT40M")
    public void fetchErrorMessages() {
        ThreadContext.put(REQUEST_ID, "error-cache-scheduler-" + Instant.now().toEpochMilli() / 1000);
        try {
            log.info("scheduler started localizationErrorCaching Scheduler at {}", LocalDateTime.now());
            SchedulerData schedulerData = schedulerServiceDao.getSchedulerData(LOCALIZATION_ERROR_CACHING_SCHEDULER);
            log.debug("Scheduler data for this run {}", schedulerData);
            Date date;
            if (schedulerData == null || errorMessagesCache == null) {
                date = new GregorianCalendar(2022, Calendar.JANUARY, 19, 10,
                        34,
                        23).getTime();
            } else {
                String schedulerDate = new String(schedulerData.getData());
                date = new SimpleDateFormat("yyyyMMddHHmmss").parse(schedulerDate);
            }
            String formattedSchedulerDate = this.getFormattedDate(date);
            Date updatedSchedulerDate = Calendar.getInstance().getTime();
            if (localeCache == null || localeCache.size() == 0) {
                log.info("No locale present in locale cache. Fetching locales");
            }
            this.setLocaleCache();
            for (String locale : localeCache) {
                if (errorMessagesCache != null && errorMessagesCache.containsKey(locale)) {
                    this.updateCacheForDeltaErrorMessages(locale, formattedSchedulerDate);
                } else {
                    this.updateCacheForNewLocale(locale);
                    log.info("Localization error message cache updated: {}", errorMessagesCache);
                    String updatedFormattedSchedulerDate = this.getFormattedDate(updatedSchedulerDate);
                    if (schedulerData == null) {
                        SchedulerData newSchedulerData = new SchedulerData();
                        newSchedulerData.setSchedulerName(LOCALIZATION_ERROR_CACHING_SCHEDULER);
                        newSchedulerData.setData(updatedFormattedSchedulerDate.getBytes());
                        schedulerServiceDao.saveSchedulerData(newSchedulerData);
                    } else {
                        schedulerData.setData(updatedFormattedSchedulerDate.getBytes());
                        schedulerServiceDao.saveSchedulerData(schedulerData);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error occurred while populating error cache: {}", CommonsUtility.exceptionFormatter(ex));
        } finally {
            log.info("scheduler completed localizationErrorCaching Scheduler at {}", LocalDateTime.now());
            ThreadContext.clearMap();
        }
    }

    private void setLocaleCache() throws Exception {
        try {
            log.info("Setting locale cache");
            LocaleResponse localeResponse = localizationClient.getLocaleList();
            Map<String, String> localeMap = new HashMap<>();
            if (localeResponse != null && localeResponse.getResponse() != null) {
                localeMap = localeResponse.getResponse();
                localeCache = new ArrayList<>();
                for (Map.Entry<String, String> entry : localeMap.entrySet()) {
                    String locale = entry.getKey();
                    if (locale != null && !locale.equals("null")) {
                        localeCache.add(locale);
                    }
                }

                log.info("Localization language cache updated: {}", localeCache);
            } else {
                log.error("Invalid response received: {}", localeResponse);
                throw new BadRequestException("failure", 9999, "Invalid request");
            }

        } catch (Exception ex) {
            log.error("Exception occured while setting locale list: {}", CommonsUtility.exceptionFormatter(ex));
            throw ex;
        }
    }

    private void updateCacheForNewLocale(String locale) throws Exception {
        log.info("Locale: {} not present in error msg cache, updating for locale", locale);
        try {
            Boolean hasNext = true;
            Integer page = 0;
            Integer limit = 50;
            while (hasNext) {
                LocalizationErrorResponse localizationErrorResponse = localizationClient.getErrorMessages(page, limit, locale, null);
                this.saveDataInCache(localizationErrorResponse, locale);
                if (localizationErrorResponse.getResponse() != null && localizationErrorResponse.getResponse().containsKey(HAS_NEXT) && TRUE.equalsIgnoreCase(String.valueOf(localizationErrorResponse.getResponse().get(HAS_NEXT)))) {
                    page = page + 1;
                    hasNext = true;
                } else {
                    hasNext = false;
                }
            }
        } catch (Exception ex) {
            log.error("Error occured while updating error messages for locale: {}, {}", locale, CommonsUtility.exceptionFormatter(ex));

        }
    }

    private void updateCacheForDeltaErrorMessages(String locale, String formattedSchedulerDate) throws Exception {
        log.info("Locale: {} already present in error msg cache, updating for delta", locale);
        try {
            Boolean hasNext = true;
            Integer page = 0;
            Integer limit = 50;
            while (hasNext) {
                LocalizationErrorResponse localizationErrorResponse = localizationClient.getErrorMessages(page, limit, locale, formattedSchedulerDate);
                this.saveDataInCache(localizationErrorResponse, locale);
                if (localizationErrorResponse.getResponse() != null && localizationErrorResponse.getResponse().containsKey(HAS_NEXT) && TRUE.equalsIgnoreCase(String.valueOf(localizationErrorResponse.getResponse().get(HAS_NEXT)))) {
                    page = page + 1;
                    hasNext = true;
                } else {
                    hasNext = false;
                }
            }
        } catch (Exception ex) {
            log.error("Error occurred while updating delta for locale: {}, {}", locale, CommonsUtility.exceptionFormatter(ex));
        }
    }

    private void saveDataInCache(LocalizationErrorResponse localizationErrorResponse, String locale) {
        try {
            if (localizationErrorResponse == null || localizationErrorResponse.getResponse() == null) {
                log.info("Empty response map received");
                return;
            }
            if (!localizationErrorResponse.getResponse().containsKey(locale)) {
                log.info("Locale: {} not present in the response", locale);
                return;
            }
            Object errorObj = localizationErrorResponse.getResponse().get(locale);
            List<Map<String, String>> localisationErrorResponseMapList = new ArrayList<>();
            localisationErrorResponseMapList = objectMapper.convertValue(errorObj, TYPE_REFERENCE);
            if (localisationErrorResponseMapList == null) {
                return;
            }

            if (errorMessagesCache == null) {
                errorMessagesCache = new HashMap<>();
            }

            log.info("Adding messages for locale : {}", locale);
            if (errorMessagesCache.containsKey(locale)) {
                Map<String, String> errorMessageLocaleMap = errorMessagesCache.get(locale);
                for (Map<String, String> localizationErrorDataDto : localisationErrorResponseMapList) {
                    errorMessageLocaleMap.put(localizationErrorDataDto.get("key"), localizationErrorDataDto.get("message"));
                }
                errorMessagesCache.put(locale, errorMessageLocaleMap);
            } else {
                Map<String, String> errorMessageLocaleCacheMap = new HashMap<>();
                for (Map<String, String> localizationErrorDataDto : localisationErrorResponseMapList) {
                    errorMessageLocaleCacheMap.put(localizationErrorDataDto.get("key"), localizationErrorDataDto.get("message"));
                }
                errorMessagesCache.put(locale, errorMessageLocaleCacheMap);

            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private String getFormattedDate(Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String strDate = dateFormat.format(date);
            return strDate;
        } catch (Exception ex) {
            log.error("Exception occurred while parsing date: {}", CommonsUtility.exceptionFormatter(ex));
            throw ex;
        }
    }

    public String getErrorMessageForKeyAndLocale(String key, String locale) {
        try {
            log.info("Fetching error message for locale :{} and key: {}", locale, key);
            String errorMessage = null;
            if (errorMessagesCache != null && errorMessagesCache.containsKey(locale)) {
                Map<String, String> errorMessagesMap = errorMessagesCache.get(locale);
                if (errorMessagesMap.containsKey(key)) {
                    errorMessage = errorMessagesMap.get(key);
                }
            } else if (errorMessagesCache != null && errorMessagesCache.containsKey("en-IN")) {
                Map<String, String> errorMessagesMap = errorMessagesCache.get("en-IN");
                if (errorMessagesMap.containsKey(key)) {
                    errorMessage = errorMessagesMap.get(key);
                }
            }
            log.info("Error message found : {}", errorMessage);
            return errorMessage;
        } catch (Exception ex) {
            log.info("Error");
            return null;
        }
    }
}
