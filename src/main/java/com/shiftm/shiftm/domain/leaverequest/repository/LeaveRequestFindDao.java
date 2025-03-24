package com.shiftm.shiftm.domain.leaverequest.repository;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.exception.LeaveRequestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeaveRequestFindDao {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequest findById(final Long id) {
        return leaveRequestRepository.findById(id).orElseThrow(LeaveRequestNotFoundException::new);
    }
}