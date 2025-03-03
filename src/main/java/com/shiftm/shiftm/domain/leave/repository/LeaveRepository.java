package com.shiftm.shiftm.domain.leave.repository;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
}
