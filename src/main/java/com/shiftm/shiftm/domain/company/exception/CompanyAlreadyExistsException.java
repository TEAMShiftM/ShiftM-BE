package com.shiftm.shiftm.domain.company.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

public class CompanyAlreadyExistsException extends BusinessException {
    public CompanyAlreadyExistsException() {
        super(ErrorCode.COMPANY_ALREADY_EXISTS);
    }
}
