package com.shiftm.shiftm.infra.email.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class UnableToSendEmailException extends BusinessException {
    public UnableToSendEmailException() {
        super(ErrorCode.UNABLE_TO_SEND_EMAIL);
    }
}
