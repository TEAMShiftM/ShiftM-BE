package com.shiftm.shiftm.domain.auth.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

public class InvalidTokenException extends InvalidValueException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
    }
}
