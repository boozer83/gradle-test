package io.kakaoi.exception;

import io.kakaoi.config.Constants;

public class BusinessException extends RuntimeException {

    private final Constants.CommonCode errorCode;

    private final Object data;

    public BusinessException(String message, Throwable throwable, Constants.CommonCode errorCode, Object data) {
        super(message, throwable);
        this.errorCode = errorCode;
        this.data = data;
    }

    public BusinessException(String message, Throwable throwable, Constants.CommonCode errorCode) {
        this(message, throwable, errorCode, null);
    }

    public BusinessException(Throwable throwable, Constants.CommonCode errorCode) {
        this(errorCode.getMessage(), throwable, errorCode, null);
    }

    public BusinessException(String message, Constants.CommonCode errorCode) {
        this(message, null, errorCode, null);
    }

    public BusinessException(String message, Constants.CommonCode errorCode, Object data) {
        this(message, null, errorCode, data);
    }

    public BusinessException(Constants.CommonCode errorCode, Object data) {
        this(errorCode.getMessage(), null, errorCode, data);
    }

    public BusinessException(Constants.CommonCode errorCode) {
        this(errorCode.getMessage(), errorCode);
    }

    public Constants.CommonCode getErrorCode() {
        return errorCode;
    }

    public Object getData() {
        return data;
    }
}
