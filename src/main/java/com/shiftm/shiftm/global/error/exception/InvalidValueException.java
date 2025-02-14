package com.shiftm.shiftm.global.error.exception;

import com.shiftm.shiftm.global.error.ErrorCode;

public class InvalidValueException extends BusinessException {
    public InvalidValueException(final String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(final String value, final ErrorCode errorCode) {
        super(value, errorCode);
    }
}
