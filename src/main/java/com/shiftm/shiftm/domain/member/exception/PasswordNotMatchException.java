package com.shiftm.shiftm.domain.member.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

public class PasswordNotMatchException extends InvalidValueException {

    public PasswordNotMatchException() {
        super("password", ErrorCode.PASSWORD_NOT_MATCH);
    }
}
