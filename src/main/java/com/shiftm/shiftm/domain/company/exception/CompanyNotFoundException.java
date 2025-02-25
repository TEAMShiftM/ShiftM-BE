package com.shiftm.shiftm.domain.company.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.EntityNotFoundException;

public class CompanyNotFoundException extends EntityNotFoundException {

    public CompanyNotFoundException() {
        super("Company Is Not Found", ErrorCode.COMPANY_NOT_FOUND);
    }
}
