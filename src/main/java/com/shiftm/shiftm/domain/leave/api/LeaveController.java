package com.shiftm.shiftm.domain.leave.api;

import com.shiftm.shiftm.domain.leave.dto.response.LeaveCountResponse;
import com.shiftm.shiftm.domain.leave.service.LeaveService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/leave")
@RestController
public class LeaveController {

    private final LeaveService leaveService;

    @GetMapping("/{leaveTypeId}")
    public LeaveCountResponse getCountByLeaveType(@AuthId final String memberId, @PathVariable("leaveTypeId") final Long leaveTypeId) {
        return leaveService.getCountByLeaveType(memberId, leaveTypeId);
    }
}
