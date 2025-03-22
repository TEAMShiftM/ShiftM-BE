package com.shiftm.shiftm.domain.leave.domain;

import java.time.LocalDate;

public class LeaveBuilder {
    public static Leave build() {
        final Double count = 10.0;
        final LocalDate expirationDate = LocalDate.of(2025, 12, 31);

        return Leave.builder()
                .count(count)
                .expirationDate(expirationDate)
                .build();
    }
}