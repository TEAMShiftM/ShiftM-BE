package com.shiftm.shiftm.domain.shift.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

public class DuplicatedCheckinException extends InvalidValueException {

    public DuplicatedCheckinException(final String date) {
        super(date, ErrorCode.DUPLICATED_CHECKIN);
    }
}
