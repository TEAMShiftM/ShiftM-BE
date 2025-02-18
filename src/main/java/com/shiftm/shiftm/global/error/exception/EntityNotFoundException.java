package com.shiftm.shiftm.global.error.exception;

import com.shiftm.shiftm.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(final String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(final String message, final ErrorCode errorCode) {
        super(message, errorCode);
    }
}
