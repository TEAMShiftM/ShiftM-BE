package com.shiftm.shiftm.domain.leave.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

public class DuplicatedNameException extends InvalidValueException {
    public DuplicatedNameException(final String name) {
        super(name, ErrorCode.DUPLICATED_NAME);
    }
}
