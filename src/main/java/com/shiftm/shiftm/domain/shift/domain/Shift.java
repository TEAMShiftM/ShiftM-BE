package com.shiftm.shiftm.domain.shift.domain;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update shifts set deleted = 1 where id = ?")
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

    @Column
    private int deleted = 0; // TODO deletedAt으로 변경 (삭제 시간 저장)

    @Builder
    public Shift(final Checkin checkin, final Checkout checkout, final Member member) {
        this.checkin = checkin;
        this.checkout = checkout;
        this.member = member;
    }

    public void checkout(final LocalDateTime checkoutTime) {
        this.checkout = new Checkout(checkoutTime);
    }

    public void updateStatus(final Status status) {
        this.checkin = new Checkin(checkin.getCheckinTime(), checkin.getLatitude(), checkin.getLongitude(), status);
    }

    public void update(final LocalDateTime checkinTime, final Double latitude, final Double longitude, final Status status, final LocalDateTime checkoutTime) {
        this.checkin = new Checkin(checkinTime, latitude, longitude, status);
        this.checkout = new Checkout(checkoutTime);
    }
}
