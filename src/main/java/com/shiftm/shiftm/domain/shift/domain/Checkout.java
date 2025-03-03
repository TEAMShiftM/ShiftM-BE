package com.shiftm.shiftm.domain.shift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Checkout {
    @Column(nullable = false)
    private LocalDateTime checkoutTime;

    @Builder
    public Checkout(final LocalDateTime checkoutTime) {
        this.checkoutTime = checkoutTime;
    }
}
