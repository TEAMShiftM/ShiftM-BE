package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;

import java.time.LocalDateTime;

public record AdminAfterCheckinResponse(
        Long id,
        String name,
        String address,
        LocalDateTime checkinTime,
        Status status
) {
    public AdminAfterCheckinResponse(final Shift shift) {
        this(shift.getId(), shift.getMember().getName(), shift.getCheckin().getAddress(), shift.getCheckin().getCheckinTime(), shift.getCheckin().getStatus());
    }
}