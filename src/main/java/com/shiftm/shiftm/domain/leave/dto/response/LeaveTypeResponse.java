package com.shiftm.shiftm.domain.leave.dto.response;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;

public record LeaveTypeResponse(
        Long id,
        String name
) {
    public LeaveTypeResponse(final LeaveType leaveType) {
        this(leaveType.getId(), leaveType.getName());
    }
}
