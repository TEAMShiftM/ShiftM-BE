package com.shiftm.shiftm.domain.company.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

public class DuplicatedCompanyIdException extends InvalidValueException {
    public DuplicatedCompanyIdException(final String name) {
        super(name, ErrorCode.DUPLICATED_COMPANY_ID);
    }
}
