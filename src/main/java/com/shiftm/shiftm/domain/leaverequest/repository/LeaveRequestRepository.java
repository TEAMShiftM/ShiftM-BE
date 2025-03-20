package com.shiftm.shiftm.domain.leaverequest.repository;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByMember(final Member member);

    Page<LeaveRequest> findByMember(final Member member, final Pageable pageable);

    @Query("""
        SELECT lr FROM LeaveRequest lr
        WHERE lr.member = :member
        AND lr.status = 'APPROVED'
        AND (
            (lr.startDate >= :startDate AND lr.startDate <= :endDate)
            OR (lr.endDate >= :startDate AND lr.endDate <= :endDate)
            OR (lr.startDate <= :startDate AND lr.endDate >= :endDate)
        )
    """)
    List<LeaveRequest> findApprovedLeaves(final Member member, final LocalDate startDate, final LocalDate endDate);
}
