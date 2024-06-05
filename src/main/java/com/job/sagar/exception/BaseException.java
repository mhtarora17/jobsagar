package com.job.sagar.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.job.sagar.Utils.CommonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

 @EqualsAndHashCode(callSuper = true)
 @Data
 @NoArgsConstructor
 public class BaseException extends RuntimeException {
     private static final long serialVersionUID = -4686695722125842437L;
     private String status;
     private int code;
     private String customMessage;
     @JsonIgnore
     private Boolean isRetryable = false;

     public BaseException(String status, int code, String customMessage) {
         this.status = status;
         this.code = code;
         this.customMessage = customMessage;
     }
     public BaseException(String message, String status, int code, String customMessage) {
         super(message);
         this.status = status;
         this.code = code;
         this.customMessage = customMessage;
     }
     public BaseException(String message, Throwable cause, String status, int code, String customMessage) {
         super(message, cause);
         this.status = status;
         this.code = code;
         this.customMessage = customMessage;
     }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(" | Exception: " + getClass());
        if (getCause() != null)
            sb.append("| Cause: " + getCause());
        if (getMessage() != null)
            sb.append("| Exception message : " + getMessage().replaceAll(  "\"",  ""));
        if (getStatus() != null)
            sb.append("| Status: " + getStatus());
            sb.append("| Code: " + this.getCode());
        if (getCustomMessage() != null)
            sb.append("| Custom message :" + getCustomMessage());
        if (getStackTrace() != null && !CommonUtils.errorCodesToSkipStackTrace.contains(this.getCode()))
            sb.append("| StackTrace: " + StringUtils.join(
                    Arrays.asList(getStackTrace()).subList (0, Math.min(getStackTrace().length, 15)),
                    "----"));
        return sb.toString();
    }

     public String getStatus() {
         return status;
     }

     public void setStatus(String status) {
         this.status = status;
     }

     public int getCode() {
         return code;
     }

     public void setCode(int code) {
         this.code = code;
     }

     public String getCustomMessage() {
         return customMessage;
     }

     public void setCustomMessage(String customMessage) {
         this.customMessage = customMessage;
     }
 }
