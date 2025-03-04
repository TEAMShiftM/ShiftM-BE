package com.shiftm.shiftm.domain.shift.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CheckoutRequest(
        @NotNull
        LocalDateTime checkoutTime
) {
}
