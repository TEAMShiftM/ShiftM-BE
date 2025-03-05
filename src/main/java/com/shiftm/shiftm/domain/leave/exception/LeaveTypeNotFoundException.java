package com.shiftm.shiftm.domain.leave.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.EntityNotFoundException;

public class LeaveTypeNotFoundException extends EntityNotFoundException {
    public LeaveTypeNotFoundException(final Long target) {
        super(target + " Is Not Found", ErrorCode.LEAVE_TYPE_NOT_FOUND);
    }
}