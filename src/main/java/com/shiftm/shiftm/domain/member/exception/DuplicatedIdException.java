package com.shiftm.shiftm.domain.member.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

public class DuplicatedIdException extends InvalidValueException {
    public DuplicatedIdException(final String id) {
        super(id, ErrorCode.DUPLICATED_ID);
    }
}
