package com.shiftm.shiftm.domain.leave.dto.response;

import com.shiftm.shiftm.domain.leave.domain.Leave;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveResponse(
        Long leaveId,
        Double count,
        Double usedCount,
        Long leaveTypeId,
        String leaveType,
        LocalDate expirationDate,
        LocalDateTime createdAt
) {
    public LeaveResponse(final Leave leave) {
        this(leave.getId(), leave.getCount(), leave.getUsedCount(), leave.getLeaveType().getId(),
                leave.getLeaveType().getName(), leave.getExpirationDate(), leave.getCreatedAt());
    }
}