package com.shiftm.shiftm.domain.leave.api;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveRequest;
import com.shiftm.shiftm.domain.leave.dto.request.UpdateLeaveRequest;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveResponse;
import com.shiftm.shiftm.domain.leave.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<LeaveResponse> getLeaves() {
        List<Leave> leaves = leaveService.getLeaves();

        return leaves.stream()
                .map(LeaveResponse::new)
                .toList();
    }

    @PatchMapping("/{memberId}")
    public List<LeaveResponse> getLeave(@PathVariable("memberId") final String memberId) {
        List<Leave> leaves = leaveService.getLeave(memberId);

        return leaves.stream()
                .map(LeaveResponse::new)
                .toList();
    }
}