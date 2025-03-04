package com.shiftm.shiftm.domain.shift.api;

import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.dto.request.ShiftStatusRequest;
import com.shiftm.shiftm.domain.shift.dto.response.AdminShiftListResponse;
import com.shiftm.shiftm.domain.shift.dto.response.AdminShiftResponse;
import com.shiftm.shiftm.domain.shift.dto.response.AfterCheckinListResponse;
import com.shiftm.shiftm.domain.shift.dto.response.AfterCheckinResponse;
import com.shiftm.shiftm.domain.shift.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/admin/shift")
@RestController
public class AdminShiftController {

    private final ShiftService shiftService;

    // 전체 근무 기록 조회
    @GetMapping
    public AdminShiftListResponse getShifts(@RequestParam(defaultValue = "0") final int page,
                                            @RequestParam(defaultValue = "10") final int size,
                                            @RequestParam(required = false) final String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shift> shiftPage = shiftService.getShifts(pageable, name);
        List<AdminShiftResponse> shifts = shiftPage.getContent().stream()
                .map(shift -> new AdminShiftResponse(shift))
                .collect(Collectors.toList());
        return new AdminShiftListResponse(shifts, page, size, shiftPage.getTotalPages(), shiftPage.getTotalElements());
    }

    // 사후 출근 신청 조회
    @GetMapping("/after-checkin")
    public AfterCheckinListResponse getAfterCheckin(@RequestParam(defaultValue = "0") final int page,
                                                    @RequestParam(defaultValue = "10") final int size,
                                                    @RequestParam(required = false) final String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shift> shiftPage = shiftService.getAfterCheckin(pageable, name);
        final List<AfterCheckinResponse> shifts = shiftPage.getContent().stream()
                .map(shift -> new AfterCheckinResponse(shift))
                .collect(Collectors.toList());
        return new AfterCheckinListResponse(shifts, page, size, shiftPage.getTotalPages(), shiftPage.getTotalElements());
    }

    // 출근 상태 변경
    @PatchMapping("/{shiftId}/status")
    public AfterCheckinResponse updateAfterCheckinStatus(@PathVariable final Long shiftId, @Valid @RequestBody final ShiftStatusRequest requestDto) {
        return new AfterCheckinResponse(shiftService.updateAfterCheckinStatus(shiftId, requestDto));
    }
}
