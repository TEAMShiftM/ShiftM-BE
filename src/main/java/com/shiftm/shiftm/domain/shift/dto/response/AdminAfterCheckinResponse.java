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
    public AdminAfterCheckinResponse(final Shift shift, final String address) {
        this(shift.getId(), shift.getMember().getName(), address, shift.getCheckin().getCheckinTime(), shift.getCheckin().getStatus());
    }
}