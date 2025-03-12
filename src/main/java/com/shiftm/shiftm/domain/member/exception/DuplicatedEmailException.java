package com.shiftm.shiftm.domain.member.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

public class DuplicatedEmailException extends InvalidValueException {

    public DuplicatedEmailException(final String email) {
        super(email, ErrorCode.DUPLICATED_EMAIL);
    }
}
