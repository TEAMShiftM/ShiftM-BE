package com.shiftm.shiftm.domain.leave.dto.response;

import com.shiftm.shiftm.domain.leave.domain.Leave;

import java.time.LocalDate;

public record LeaveResponse(
        String memberId,
        String memberName,
        Long leaveId,
        Double count,
        Double usedCount,
        String leaveType,
        LocalDate expirationDate
) {
    public LeaveResponse(final Leave leave) {
        this(leave.getMember().getId(), leave.getMember().getName(), leave.getId(), leave.getCount(),
                leave.getUsedCount(), leave.getLeaveType().getName(), leave.getExpirationDate());
    }
}