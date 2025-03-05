package com.shiftm.shiftm.domain.shift.dto.request;

import com.shiftm.shiftm.domain.shift.domain.Checkin;
import com.shiftm.shiftm.domain.shift.domain.Checkout;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ShiftRequest(
        @NotNull
        LocalDateTime checkinTime,
        @NotNull
        Double latitude,
        @NotNull
        Double longitude,
        @NotNull
        Status status,
        @NotNull
        LocalDateTime checkoutTime
) {
    public Shift toEntity() {
        return Shift.builder()
                .checkin(Checkin.builder()
                        .checkinTime(checkinTime)
                        .latitude(latitude)
                        .longitude(longitude)
                        .status(status)
                        .build())
                .checkout(Checkout.builder()
                        .checkoutTime(checkoutTime)
                        .build())
                .build();
    }
}
