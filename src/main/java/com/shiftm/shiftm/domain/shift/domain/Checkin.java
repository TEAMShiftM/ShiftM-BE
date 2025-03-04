package com.shiftm.shiftm.domain.shift.domain;

import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Checkin {
    @Column(nullable = false)
    private LocalDateTime checkinTime;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Checkin(final LocalDateTime checkinTime, final Double latitude, final Double longitude, final Status status) {
        this.checkinTime = checkinTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }
}
