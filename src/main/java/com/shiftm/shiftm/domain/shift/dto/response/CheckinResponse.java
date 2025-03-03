package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;

import java.time.LocalDateTime;

public record CheckinResponse (
        Long id,
        String memberId,
        LocalDateTime checkinTime
) {
    public CheckinResponse(final Shift shift) {
        this(shift.getId(), shift.getMember().getId(), shift.getCheckin().getCheckinTime());
    }
}
