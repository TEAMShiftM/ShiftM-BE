package com.shiftm.shiftm.domain.leave.repository;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
    boolean existsByName(final String name);

    List<LeaveType> findByDeletedAtIsNull();
}
