package com.shiftm.shiftm.global.error.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(final String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
