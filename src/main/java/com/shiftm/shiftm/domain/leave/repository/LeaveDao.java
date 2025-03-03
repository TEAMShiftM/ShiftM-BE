package com.shiftm.shiftm.domain.leave.repository;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeaveDao {

    private final LeaveRepository leaveRepository;

    public Leave findById(final Long id) {
        return leaveRepository.findById(id).orElseThrow();
    }
}