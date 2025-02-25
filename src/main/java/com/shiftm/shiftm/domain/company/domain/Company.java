package com.shiftm.shiftm.domain.company.domain;

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
public class Company {
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
    public Company(String companyId, LocalTime checkinTime, LocalTime checkoutTime, LocalTime breakStartTime, LocalTime breakEndTime, Double latitude, Double longitude) {
        this.companyId = companyId;
        this.checkinTime = checkinTime;
        this.checkoutTime = checkoutTime;
        this.breakStartTime = breakStartTime;
        this.breakEndTime = breakEndTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
