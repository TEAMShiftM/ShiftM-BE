package com.shiftm.shiftm.domain.leave.api;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveRequest;
import com.shiftm.shiftm.domain.leave.dto.request.UpdateLeaveRequest;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveListResponse;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveResponse;
import com.shiftm.shiftm.domain.leave.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public LeaveListResponse getLeaves(@RequestParam(defaultValue = "0") final int page, @RequestParam(defaultValue = "10") final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        final Page<Leave> leaves = leaveService.getLeaves(pageable);

        final List<LeaveResponse> content = leaves.getContent().stream()
                .map(LeaveResponse::new)
                .toList();

        return new LeaveListResponse(content, page, size, leaves.getTotalPages(), leaves.getTotalElements());
    }

    @PatchMapping("/{memberId}")
    public LeaveListResponse getLeave(@RequestParam(defaultValue = "0") final int page, @RequestParam(defaultValue = "10") final int size,
                                      @PathVariable("memberId") final String memberId) {
        final Pageable pageable = PageRequest.of(page, size);

        final Page<Leave> leaves = leaveService.getLeave(pageable, memberId);

        final List<LeaveResponse> content = leaves.getContent().stream()
                .map(LeaveResponse::new)
                .toList();

        return new LeaveListResponse(content, page, size, leaves.getTotalPages(), leaves.getTotalElements());
    }
}