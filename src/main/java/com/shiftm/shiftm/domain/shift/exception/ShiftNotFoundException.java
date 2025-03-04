package com.shiftm.shiftm.domain.shift.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.EntityNotFoundException;

public class ShiftNotFoundException extends EntityNotFoundException {

    public ShiftNotFoundException() {
        super(ErrorCode.SHIFT_NOT_FOUND.getMessage(), ErrorCode.SHIFT_NOT_FOUND);
    }
}
