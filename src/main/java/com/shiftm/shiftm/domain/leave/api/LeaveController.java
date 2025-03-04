package com.shiftm.shiftm.domain.leave.api;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveCountResponse;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveListResponse;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveResponse;
import com.shiftm.shiftm.domain.leave.service.LeaveService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/leave")
@RestController
public class LeaveController {

    private final LeaveService leaveService;

    @GetMapping("/{leaveTypeId}")
    public LeaveCountResponse getCountByLeaveType(@AuthId final String memberId, @PathVariable("leaveTypeId") final Long leaveTypeId) {
        return leaveService.getCountByLeaveType(memberId, leaveTypeId);
    }

    @GetMapping
    public LeaveListResponse getLeaveInfo(@AuthId final String memberId, @RequestParam(defaultValue = "0") final int page,
                                          @RequestParam(defaultValue = "10") final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        final Page<Leave> leaves = leaveService.getLeaveInfo(memberId, pageable);
        System.out.println(memberId);
        final List<LeaveResponse> content = leaves.getContent().stream()
                .map(LeaveResponse::new)
                .toList();

        return new LeaveListResponse(content, page, size, leaves.getTotalPages(), leaves.getTotalElements());
    }
}
