package com.shiftm.shiftm.domain.leave.api;

import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveRequest;
import com.shiftm.shiftm.domain.leave.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin/leave")
@RestController
public class AdminLeaveController {

    private final LeaveService leaveService;

    @PostMapping
    public void createLeaves(@Valid @RequestBody final CreateLeaveRequest requestDto) {
        leaveService.createLeaves(requestDto);
    }
}