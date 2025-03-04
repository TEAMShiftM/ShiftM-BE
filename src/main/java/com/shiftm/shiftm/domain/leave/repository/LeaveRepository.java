package com.shiftm.shiftm.domain.leave.repository;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @Query("SELECT leave FROM Leave leave JOIN LeaveType leavetype on leave.leaveType = leavetype " +
            "where leave.expirationDate >= :today AND leave.member.id = :memberId " +
            "AND leavetype.id = :leaveTypeId " +
            "AND leave.count - leave.usedCount > 0 ORDER BY leave.expirationDate ASC ")
    List<Leave> findLeaves(@Param("memberId") final String memberId, @Param("leaveTypeId") final Long leaveTypeId, @Param("today") final LocalDate today);

    List<Leave> findByMember(final Member member);
}
