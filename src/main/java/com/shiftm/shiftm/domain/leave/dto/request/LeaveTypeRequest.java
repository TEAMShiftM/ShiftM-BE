package com.shiftm.shiftm.domain.leave.dto.request;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;

public record LeaveTypeRequest(
        String name
) {
    public LeaveType toEntity(final String name) {
        return LeaveType.builder()
                .name(name)
                .build();
    }
}
