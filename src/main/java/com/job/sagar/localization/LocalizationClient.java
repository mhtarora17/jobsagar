package com.job.sagar.localization;

import com.job.sagar.response.LocaleResponse;
import com.job.sagar.response.LocalizationErrorResponse;

public interface LocalizationClient {

    LocalizationErrorResponse getErrorMessages(Integer pageNum, Integer limit, String locale, String timestamp) throws Exception;

    LocaleResponse getLocaleList() throws Exception;

}
