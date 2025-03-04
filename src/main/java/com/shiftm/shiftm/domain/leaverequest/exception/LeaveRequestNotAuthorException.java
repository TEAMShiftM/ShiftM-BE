package com.shiftm.shiftm.domain.leaverequest.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class LeaveRequestNotAuthorException extends BusinessException {
    public LeaveRequestNotAuthorException(final ErrorCode errorCode) {
        super(errorCode);
    }
}