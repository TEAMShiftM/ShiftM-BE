package com.shiftm.shiftm.domain.leave.exception;

import com.shiftm.shiftm.global.error.exception.EntityNotFoundException;

public class LeaveNotFoundException extends EntityNotFoundException {
    public LeaveNotFoundException(final Long target) {
        super(target + " Is Not Found");
    }
}