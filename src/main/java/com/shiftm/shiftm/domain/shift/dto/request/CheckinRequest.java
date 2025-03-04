package com.shiftm.shiftm.domain.shift.dto.request;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.shift.domain.Checkin;
import com.shiftm.shiftm.domain.shift.domain.Checkout;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CheckinRequest(
        @NotNull
        LocalDateTime checkinTime
) {
    public Shift toEntity(final Member member) {
        return Shift.builder()
                .checkin(Checkin.builder()
                        .checkinTime(checkinTime)
                        .latitude(null)
                        .longitude(null)
                        .status(Status.AUTO_APPROVED)
                        .build())
                .checkout(null)
                .member(member)
                .build();
    }
}
