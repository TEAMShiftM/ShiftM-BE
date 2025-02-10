package com.shiftm.shiftm.domain.company.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime checkinTime;

    @Column(nullable = false)
    private LocalDateTime checkoutTime;

    @Column(nullable = false)
    private LocalDateTime breakStartTime;

    @Column(nullable = false)
    private LocalDateTime breakEndTime;

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}
