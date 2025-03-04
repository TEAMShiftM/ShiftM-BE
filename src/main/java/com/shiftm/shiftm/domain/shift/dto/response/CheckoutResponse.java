package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;

import java.time.LocalDateTime;

public record CheckoutResponse(
        Long id,
        LocalDateTime checkinTime,
        LocalDateTime checkoutTime
) {
    public CheckoutResponse(final Shift shift) {
        this(shift.getId(), shift.getCheckin().getCheckinTime(), shift.getCheckout().getCheckoutTime());
    }
}
