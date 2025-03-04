package com.shiftm.shiftm.domain.shift.repository;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    boolean existsByMemberAndCheckin_CheckinTimeBetween(Member member, LocalDateTime start, LocalDateTime end);

    Optional<Shift> findShiftByMemberAndCheckin_CheckinTimeBetween(Member member, LocalDateTime start, LocalDateTime end);

    List<Shift> findShiftsByMemberAndCheckin_CheckinTimeBetween(Member member, LocalDateTime start, LocalDateTime end);
}
