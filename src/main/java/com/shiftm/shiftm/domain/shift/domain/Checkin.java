package com.shiftm.shiftm.domain.shift.domain;

import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
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
    private Status status;
}
