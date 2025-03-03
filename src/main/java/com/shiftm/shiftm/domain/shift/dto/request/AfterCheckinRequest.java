package com.shiftm.shiftm.domain.shift.dto.request;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.shift.domain.Checkin;
import com.shiftm.shiftm.domain.shift.domain.Checkout;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AfterCheckinRequest (
        @NotNull
        LocalDateTime checkinTime,
        @NotNull
        Double latitude,
        @NotNull
        Double longitude
) {
    public Shift toEntity(final Member member) {
        return Shift.builder()
                .checkin(Checkin.builder()
                        .checkinTime(checkinTime)
                        .latitude(latitude)
                        .longitude(longitude)
                        .status(Status.PENDING)
                        .build())
                .checkout(Checkout.builder()
                        .checkoutTime(checkinTime) //TODO 퇴근 시간 기본값 설정
                        .build())
                .member(member)
                .build();
    }
}