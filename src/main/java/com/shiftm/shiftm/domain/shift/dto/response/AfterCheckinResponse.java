package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;

import java.time.LocalDateTime;

public record AfterCheckinResponse (
        Long id,
        LocalDateTime checkinTime,
        Double latitude,
        Double longitude,
        Status status
) {
    public AfterCheckinResponse(final Shift shift) {
        this(shift.getId(), shift.getCheckin().getCheckinTime(), shift.getCheckin().getLatitude(),
                shift.getCheckin().getLongitude(), shift.getCheckin().getStatus());
    }
}