package com.github.greengerong.exception;

/**
 * Created by twer on 2/25/14.
 */
public class CommonUtilsRuntimeException extends RuntimeException {
    static final long serialVersionUID = -7034897190745712939L;

    public CommonUtilsRuntimeException() {
    }

    public CommonUtilsRuntimeException(java.lang.String message) {
        super(message);
    }

    public CommonUtilsRuntimeException(java.lang.String message, java.lang.Throwable throwable) {
        super(message, throwable);
    }

    public CommonUtilsRuntimeException(java.lang.Throwable throwable) {
        super(throwable);
    }
}