package com.shiftm.shiftm.domain.shift.repository;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Shift s " +
            "WHERE s.member = :member AND s.checkin.checkinTime >= :start AND s.checkin.checkinTime <= :end")
    boolean existsByMemberAndCheckinTimeInRange(Member member, LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Shift s WHERE s.member = :member AND s.checkin.checkinTime >= :start AND s.checkin.checkinTime <= :end")
    Optional<Shift> findShiftByMemberAndCheckinTimeInRange(Member member, LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Shift s WHERE s.member = :member AND s.checkin.checkinTime >= :start AND s.checkin.checkinTime <= :end")
    List<Shift> findShiftsByMemberAndCheckinTimeInRange(Member member, LocalDateTime start, LocalDateTime end);
}
