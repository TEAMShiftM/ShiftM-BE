package com.shiftm.shiftm.domain.leave.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class LeaveTypeLockedException extends BusinessException {
    public LeaveTypeLockedException() {
        super(ErrorCode.LEAVE_TYPE_LOCKED);
    }
}