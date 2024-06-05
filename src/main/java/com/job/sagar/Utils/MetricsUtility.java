package com.job.sagar.Utils;

import org.apache.logging.log4j.ThreadContext;

import static com.job.sagar.constant.MetricsConstants.API_NAME_FOR_METRIC;
import static com.job.sagar.constant.MetricsConstants.DOWNSTREAM_METRIC_TYPE;


public class MetricsUtility {

    public static void setDataForGetApi(String url, String api) {
        ThreadContext.put(API_NAME_FOR_METRIC, url);
        ThreadContext.put(DOWNSTREAM_METRIC_TYPE, api);
    }


}