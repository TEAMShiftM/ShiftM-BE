package com.shiftm.shiftm.domain.leaverequest.repository;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
}
