package com.shiftm.shiftm.domain.shift.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class CheckinAlreadyExistsException extends BusinessException {

    public CheckinAlreadyExistsException() {
        super(ErrorCode.CHECKIN_ALREADY_EXISTS);
    }
}
