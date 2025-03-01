package com.shiftm.shiftm.domain.company.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.EntityNotFoundException;

public class CompanyNotFoundException extends EntityNotFoundException {

    public CompanyNotFoundException() {
        super(ErrorCode.COMPANY_NOT_FOUND.getMessage(), ErrorCode.COMPANY_NOT_FOUND);
    }
}
