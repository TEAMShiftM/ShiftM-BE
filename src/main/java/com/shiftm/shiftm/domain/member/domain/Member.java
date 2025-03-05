package com.shiftm.shiftm.domain.member.domain;

import com.shiftm.shiftm.domain.member.domain.enums.Gender;
import com.shiftm.shiftm.domain.member.domain.enums.Role;
import com.shiftm.shiftm.domain.member.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
@Entity
public class Member {
    @Id
    private String id;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private LocalDate birthDate;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Setter
    @Column
    private LocalDate entryDate;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(final String id,
                  final String password,
                  final String email,
                  final String name,
                  final LocalDate birthDate,
                  final Gender gender,
                  final Status status,
                  final Role role) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.status = status;
        this.role = role;
    }
}
