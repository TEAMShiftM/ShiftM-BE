package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;

import java.time.LocalDateTime;

public record ShiftResponse(
        Long id,
        LocalDateTime checkinTime,
        LocalDateTime checkoutTime
) {
    public ShiftResponse(final Shift shift) {
        this(shift.getId(), shift.getCheckin().getCheckinTime(), shift.getCheckout().getCheckoutTime());
    }
}
