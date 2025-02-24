package com.shiftm.shiftm.domain.auth.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

public class InvalidBearerPrefixException extends InvalidValueException {
    public InvalidBearerPrefixException() {
        super(ErrorCode.INVALID_BEARER_PREFIX.getMessage(), ErrorCode.INVALID_BEARER_PREFIX);
    }
}
