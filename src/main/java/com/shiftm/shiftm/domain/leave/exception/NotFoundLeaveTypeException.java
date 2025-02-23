package com.shiftm.shiftm.domain.leave.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class NotFoundLeaveTypeException extends BusinessException {
    public NotFoundLeaveTypeException() {
        super(ErrorCode.NOT_FOUND_LEAVE_TYPE);
    }
}
