package com.shiftm.shiftm.domain.shift.domain;

import com.shiftm.shiftm.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "shifts")
@Entity
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Checkin checkin;

    @Embedded
    private Checkout checkout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Shift(Checkin checkin, Checkout checkout, Member member) {
        this.checkin = checkin;
        this.checkout = checkout;
        this.member = member;
    }

    public void checkout(final LocalDateTime checkoutTime) {
        this.checkout = new Checkout(checkoutTime);
    }
}
