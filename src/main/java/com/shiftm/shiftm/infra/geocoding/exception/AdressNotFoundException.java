package com.shiftm.shiftm.infra.geocoding.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class AdressNotFoundException extends BusinessException {

    public AdressNotFoundException() {
        super(ErrorCode.ADDRESS_NOT_FOUND);
    }
}
