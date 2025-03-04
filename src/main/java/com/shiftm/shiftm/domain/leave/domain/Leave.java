package com.shiftm.shiftm.domain.leave.domain;

import com.shiftm.shiftm.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "leaves")
@Entity
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double count;

    @Column(nullable = false)
    private Double usedCount;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private LeaveType leaveType;

    @Builder
    public Leave(final Double count, final LocalDate expirationDate) {
        this.count = count;
        this.usedCount = 0.0;
        this.expirationDate = expirationDate;
    }

    public void updateMember(final Member member) {
        this.member = member;
    }

    public void updateLeaveType(final LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public void updateLeave(final Double count, final Double usedCount, final LocalDate expirationDate) {
        this.count = count;
        this.usedCount = usedCount;
        this.expirationDate = expirationDate;
    }

    public void updateUsedCount(final Double usedCount) {
        this.usedCount = usedCount;
    }
}
