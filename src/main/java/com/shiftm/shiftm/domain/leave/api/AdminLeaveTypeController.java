package com.shiftm.shiftm.domain.leave.api;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveTypeRequest;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveTypeResponse;
import com.shiftm.shiftm.domain.leave.service.LeaveTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/leave-type")
@RestController
public class AdminLeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @PostMapping
    public ResponseEntity<LeaveTypeResponse> createLeaveType(@Valid @RequestBody final CreateLeaveTypeRequest requestDto) {
        final LeaveType leaveType = leaveTypeService.createLeaveType(requestDto);
        return ResponseEntity.ok(new LeaveTypeResponse(leaveType));
    }
}
