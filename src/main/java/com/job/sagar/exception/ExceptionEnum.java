package com.job.sagar.exception;

import static com.paytm.bank.constant.Constants.FAILED;
import static com.paytm.bank.constant.Constants.INTERNAL_SERVER_ERROR;
import static com.paytm.bank.constant.Constants.IS_OLD_API;
import static com.paytm.bank.constant.Constants.PENDING;
import static com.paytm.bank.constant.ErrorCodesConstant.BAD_REQUEST_ERROR_CODE;
import static com.paytm.bank.constant.ErrorCodesConstant. GONE_ERROR_CODE;
import static com.paytm.bank.constant.ErrorCodesConstant.INTEGRATION_SERVICE_UNAVAILABLE_ERROR_CODE;
import static com.paytm.bank.constant.ErrorCodesConstant.INTERNAL_SERVER_ERROR_CODE;
import static com.paytm.bank.constant.ErrorCodesConstant.MICRO_SERVICE_ERROR_CODE;
import static com.paytm.bank.constant.ErrorCodesConstant.NOT_FOUND_ERROR_CODE;
import static com.paytm.bank.constant.ErrorCodesConstant. UNAUTHORIZED_ERROR_CODE;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

public enum ExceptionEnum {
    BAD_REQUEST_EXCEPTION(400) {
        @Override
        public BaseException throwException() {
            String is0ldApi = ThreadContext.get(IS_OLD_API);
            if (StringUtils.isEmpty(isoldApi) || Boolean.TRUE.toString().equalsIgnoreCase(isoldApi)) {
                return new BaseException(FAILED, BAD_REQUEST_ERROR_CODE, INTERNAL_SERVER_ERROR);
            } else {
                return new ExternalServiceErrorException(FAILED, BAD_REQUEST_ERROR_CODE, INTERNAL_SERVER_ERROR);
            }
        }
    },

    UNAUTHORIZED_EXCEPTION(401) {
        @Override
        public BaseException throwException() {
            String is0ldApi = ThreadContext.get(IS_OLD_API);
            if (StringUtils.isEmpty(isoldApi) | Boolean.TRUE.toString().equalsIgnoreCase(is0ldApi)) {
                return new BaseException(FAILED, UNAUTHORIZED_ERROR_CODE, INTERNAL_SERVER_ERROR);
            } else {
                return new ExternalServiceErrorException(FAILED, UNAUTHORIZED_ERROR_CODE, INTERNAL_SERVER_ERROR);
            }
        }
    },
    NOT_FOUND(404) {
        @Override
        public BaseException throwException() {
            String is0ldApi = ThreadContext.get(IS_OLD_API);
            if (StringUtils.isEmpty(isOldApi) || Boolean.TRUE.toString().equalsIgnoreCase(is0ldApi)) {
                return new BaseException(FAILED, NOTFOUND_ERROR_CODE, INTERNAL_SERVER_ERROR);
            } else {
                return new ExternalServiceErrorException(FAILED, NOT_FOUND_ERROR_CODE, INTERNAL_SERVER_ERROR);

            }
        }
    },
    MICRO_SERVICE_ERROR_EXCEPTION(500) {
        @Override
        public BaseException throwException() {
            return new MicroServiceErrorException(PENDING, MICRO_SERVICE_ERROR_CODE,
                    INTERNAL_SERVER_ERROR);
        }
    },
    MICRO_SERVICE_UNAVAILABLE_EXCEPTION(503) {
        @Override
        public BaseException throwException() {
            return new MicroServiceUnavailableException(PENDING,
                    INTEGRATION_SERVICE_UNAVAILABLE_ERROR_CODE,
                    INTERNAL_SERVER_ERROR);
        }
    };
    private Integer code;

    ExceptionEnun(Integer code) { this.code = code; }

    public static BaseException getMappedException(int code) {
        for (ExceptionEnum b : ExceptionEnum.values()) {
            if (b.getCode() == code) {
                return b.throwException();
            }
        }
        return new BaseException(PENDING, INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR);
    }
    public int getCode() {return this.code; }

    public abstract BaseException throwException();
}