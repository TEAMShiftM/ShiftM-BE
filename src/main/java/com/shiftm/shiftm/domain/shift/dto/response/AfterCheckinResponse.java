package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;

import java.time.LocalDateTime;

public record AfterCheckinResponse (
        Long id,
        LocalDateTime checkinTime,
        Double latitude,
        Double longitude
) {
    public AfterCheckinResponse(final Shift shift) {
        this(shift.getId(), shift.getCheckin().getCheckinTime(),
                shift.getCheckin().getLatitude(), shift.getCheckin().getLongitude());
    }
}