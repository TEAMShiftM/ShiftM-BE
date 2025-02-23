package com.shiftm.shiftm.domain.leave.api;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.dto.request.LeaveTypeRequest;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveTypeResponse;
import com.shiftm.shiftm.domain.leave.service.LeaveTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/leave-type")
@RestController
public class AdminLeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @PostMapping
    public LeaveTypeResponse createLeaveType(@Valid @RequestBody final LeaveTypeRequest requestDto) {
        final LeaveType leaveType = leaveTypeService.createLeaveType(requestDto);
        return new LeaveTypeResponse(leaveType);
    }

    @PatchMapping("/{leaveTypeId}")
    public LeaveTypeResponse updateLeaveType(@PathVariable(name = "leaveTypeId") Long leaveTypeId,
                                                @Valid @RequestBody final LeaveTypeRequest requestDto) {
        final LeaveType leaveType = leaveTypeService.updateLeaveType(leaveTypeId, requestDto);
        return new LeaveTypeResponse(leaveType);
    }
}
