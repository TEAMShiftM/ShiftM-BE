package com.shiftm.shiftm.domain.company.domain;

import com.shiftm.shiftm.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "companies")
@Entity
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyId;

    @Column(nullable = false)
    private LocalTime checkinTime;

    @Column(nullable = false)
    private LocalTime checkoutTime;

    @Column(nullable = false)
    private LocalTime breakStartTime;

    @Column(nullable = false)
    private LocalTime breakEndTime;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Builder
    public Company(final String companyId, final LocalTime checkinTime, final LocalTime checkoutTime,
                   final LocalTime breakStartTime, final LocalTime breakEndTime, final Double latitude, final Double longitude) {
        this.companyId = companyId;
        this.checkinTime = checkinTime;
        this.checkoutTime = checkoutTime;
        this.breakStartTime = breakStartTime;
        this.breakEndTime = breakEndTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void update(final String companyId, final LocalTime checkinTime, final LocalTime checkoutTime,
                       final LocalTime breakStartTime, final LocalTime breakEndTime, final Double latitude, final Double longitude) {
        this.companyId = companyId;
        this.checkinTime = checkinTime;
        this.checkoutTime = checkoutTime;
        this.breakStartTime = breakStartTime;
        this.breakEndTime = breakEndTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
