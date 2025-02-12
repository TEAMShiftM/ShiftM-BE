package com.shiftm.shiftm.global.error.exception;

import com.shiftm.shiftm.global.error.ErrorCode;

public class InvalidValueException extends BusinessException {
    public InvalidValueException(final String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(final String message, final ErrorCode errorCode) {
        super(message, errorCode);
    }
}
