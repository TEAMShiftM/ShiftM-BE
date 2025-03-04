package com.shiftm.shiftm.domain.shift.repository;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Shift s " +
            "WHERE s.member = :member AND s.checkin.checkinTime >= :start AND s.checkin.checkinTime <= :end")
    boolean existsByMemberAndCheckinTimeInRange(Member member, LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Shift s WHERE s.member = :member AND s.checkin.checkinTime >= :start AND s.checkin.checkinTime <= :end " +
            "AND s.deleted = 0")
    Optional<Shift> findShiftByMemberAndCheckinTimeInRange(Member member, LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Shift s WHERE s.member = :member " +
            "AND (:start IS NULL OR s.checkin.checkinTime >= :start) " +
            "AND (:end IS NULL OR s.checkin.checkinTime <= :end) " +
            "AND s.deleted = 0")
    List<Shift> findShiftsByMemberAndCheckinTimeInRange(Member member, LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Shift s WHERE s.member.name = :name")
    Page<Shift> findByName(final Pageable pageable, final String name);

    @Query("SELECT s FROM Shift s WHERE s.checkin.status != :status " +
            "ORDER BY CASE " +
            "WHEN s.checkin.status = 'PENDING' THEN 1 " +
            "WHEN s.checkin.status = 'APPROVED' THEN 2 " +
            "WHEN s.checkin.status = 'REJECTED' THEN 3 " +
            "ELSE 4 END")
    Page<Shift> findByStatusExcludeOrdered(final Pageable pageable, final Status status);

    @Query("SELECT s FROM Shift s WHERE s.checkin.status != :status AND s.member.name = :name " +
            "ORDER BY CASE " +
            "WHEN s.checkin.status = 'PENDING' THEN 1 " +
            "WHEN s.checkin.status = 'APPROVED' THEN 2 " +
            "WHEN s.checkin.status = 'REJECTED' THEN 3 " +
            "ELSE 4 END")
    Page<Shift> findByStatusExcludeAndNameOrdered(final Pageable pageable, final Status status, final String name);
}
