package com.shiftm.shiftm.domain.leave.dto.response;

import com.shiftm.shiftm.domain.leave.domain.Leave;

import java.time.LocalDate;

public record LeaveResponse(
        String memberId,
        String leaveName,
        LocalDate expirationDate,
        Integer count
) {
    public LeaveResponse(final Leave leave) {
        this(leave.getMember().getId(), leave.getLeaveType().getName(), leave.getExpirationDate(), leave.getCount());
    }
}