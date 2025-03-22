package com.shiftm.shiftm.domain.leave.dto.response;

import com.shiftm.shiftm.domain.leave.domain.Leave;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminLeaveResponse(
        String memberId,
        String memberName,
        Long leaveId,
        Double count,
        Double usedCount,
        Long leaveTypeId,
        String leaveType,
        LocalDate expirationDate,
        LocalDateTime createdAt
) {
    public AdminLeaveResponse(final Leave leave) {
        this(leave.getMember().getId(), leave.getMember().getName(), leave.getId(), leave.getCount(),
                leave.getUsedCount(), leave.getLeaveType().getId(), leave.getLeaveType().getName(),
                leave.getExpirationDate(), leave.getCreatedAt());
    }
}