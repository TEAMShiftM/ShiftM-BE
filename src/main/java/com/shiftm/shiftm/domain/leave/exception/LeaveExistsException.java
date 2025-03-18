package com.shiftm.shiftm.domain.leave.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class LeaveExistsException extends BusinessException {
    public LeaveExistsException() {
        super(ErrorCode.LEAVE_EXISTS);
    }
}