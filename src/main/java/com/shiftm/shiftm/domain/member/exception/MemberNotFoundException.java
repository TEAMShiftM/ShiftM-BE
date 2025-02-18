package com.shiftm.shiftm.domain.member.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {
    public MemberNotFoundException(final String target) {
        super(target + " Is Not Found", ErrorCode.MEMBER_NOT_FOUND);
    }
}
