package com.shiftm.shiftm.domain.leave.api;

import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveRequest;
import com.shiftm.shiftm.domain.leave.dto.request.UpdateLeaveRequest;
import com.shiftm.shiftm.domain.leave.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/leave")
@RestController
public class AdminLeaveController {

    private final LeaveService leaveService;

    @PostMapping
    public void createLeaves(@Valid @RequestBody final CreateLeaveRequest requestDto) {
        leaveService.createLeaves(requestDto);
    }

    @PatchMapping("/{leaveId}")
    public void updateLeave(@PathVariable("leaveId") final Long leaveId, @Valid @RequestBody final UpdateLeaveRequest requestDto) {
        leaveService.updateLeave(leaveId, requestDto);
    }
}