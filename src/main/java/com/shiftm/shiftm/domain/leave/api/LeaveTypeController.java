package com.shiftm.shiftm.domain.leave.api;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveTypeResponse;
import com.shiftm.shiftm.domain.leave.service.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/leave-type")
@RestController
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @GetMapping
    public List<LeaveTypeResponse> getLeaveTypes() {
        final List<LeaveType> leaveTypes = leaveTypeService.getLeaveTypes();

        return leaveTypes.stream()
                .map(LeaveTypeResponse::new)
                .toList();
    }
}
