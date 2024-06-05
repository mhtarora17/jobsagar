package com.job.sagar.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
public class CommonsUtility {
    public CommonsUtility() {
    }

    public static long getEpochDateFromDate(String date) {
        if (StringUtils.isEmpty(date)) {
            throw new IllegalArgumentException("Invalid Date passed" + date);
        } else {
            String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";
            return getEpochDateFromDate(date, defaultDateFormat);
        }
    }

    public static long getEpochDateFromDate(String date, String dateFormat) throws IllegalArgumentException {
        if (StringUtils.isEmpty(date)) {
            throw new IllegalArgumentException("Invalid Date passed" + date);
        } else {
            if (StringUtils.isEmpty(dateFormat)) {
                dateFormat = "yyyy-MM-dd HH:mm:ss";
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date parsedDate = sdf.parse(date);
            return parsedDate.getTime();
        } catch (ParseException var4) {
            throw new IllegalArgumentException("Invalid Date passed" + date);
        }
    }

    public static String getDateFromEpochTime(String timeStr, String dateFormat) throws IllegalArgumentException {
        if (StringUtils.isEmpty(dateFormat)) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        if (StringUtils.isEmpty(timeStr)) {
            throw new IllegalArgumentException("Invalid epoch time passed" + timeStr);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            try {
                Long time = Long.parseLong(timeStr.length() == 10 ? timeStr + "000" : timeStr);
                String parsedDate = sdf.format(new Date(time));
                return parsedDate;
            } catch (Exception var6) {
                throw new IllegalArgumentException("Invalid epoch time passed" + timeStr);
            }
        }
    }

    public static String exceptionFormatter(Exception e) {
        StringBuilder sb = new StringBuilder(" | Exception : " + e.getClass());
        if (e.getCause() != null) {
            sb.append(" | Cause: " + e.getCause());
        }
        if (e.getMessage() != null) {
            String var10001 = e.getMessage();
            sb.append(" | Exception message : " + var10001.replaceAll("\"", ""));
        }
        if (e.getStackTrace() != null) {
            List var2 = Arrays.asList(e.getStackTrace()).subList (0, Math.min (e.getStackTrace().length, 15));
            sb.append("StackTrace : " + StringUtils.join(var2, "----"));
        }
        return sb.toString();
    }
}
