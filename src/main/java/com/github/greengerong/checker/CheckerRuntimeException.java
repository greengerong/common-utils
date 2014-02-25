package com.github.greengerong.checker;

import com.github.greengerong.exception.CommonUtilsRuntimeException;

import static com.google.common.base.Preconditions.checkNotNull;


public class CheckerRuntimeException extends CommonUtilsRuntimeException {
    static final long serialVersionUID = -7034884190745712939L;

    public CheckerRuntimeException() {
    }

    public CheckerRuntimeException(java.lang.String message) {
        super(message);
    }

    public CheckerRuntimeException(java.lang.String message, java.lang.Throwable throwable) {
        super(message, throwable);
    }

    public CheckerRuntimeException(java.lang.Throwable throwable) {
        super(throwable);
    }
}
