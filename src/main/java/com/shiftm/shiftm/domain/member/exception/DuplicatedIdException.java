package com.shiftm.shiftm.domain.member.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class DuplicatedIdException extends BusinessException {
    public DuplicatedIdException(final String id) {
        super(id, ErrorCode.DUPLICATED_ID);
    }
}
