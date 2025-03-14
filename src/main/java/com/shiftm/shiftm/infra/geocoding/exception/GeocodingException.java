package com.shiftm.shiftm.infra.geocoding.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class GeocodingException extends BusinessException {

    public GeocodingException() {
        super(ErrorCode.GEOCODING_FAILED);
    }
}
