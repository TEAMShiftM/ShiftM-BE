package com.shiftm.shiftm.domain.shift.api;

import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.dto.request.AfterCheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckoutRequest;
import com.shiftm.shiftm.domain.shift.dto.response.*;
import com.shiftm.shiftm.domain.shift.service.ShiftService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/shift")
@RestController
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping("/check-in")
    public CheckinResponse createCheckin(@AuthId final String memberId, @Valid @RequestBody final CheckinRequest requestDto) {
        final Shift shift = shiftService.createCheckin(memberId, requestDto);
        return new CheckinResponse(shift);
    }

    @PatchMapping("/check-out")
    public CheckoutResponse createCheckout(@AuthId final String memberId, @Valid @RequestBody final CheckoutRequest requestDto) {
        final Shift shift = shiftService.createCheckout(memberId, requestDto);
        return new CheckoutResponse(shift);
    }

    @GetMapping
    public ShiftListResponse getShiftsInRange(@AuthId final String memberId,
                                              @RequestParam(required = false) final LocalDate startDate,
                                              @RequestParam(required = false) final LocalDate endDate) {
        final List<ShiftResponse> shifts = shiftService.getShiftsInRange(memberId, startDate, endDate).stream()
                .map(shift -> new ShiftResponse(shift))
                .collect(Collectors.toList());
        return new ShiftListResponse(shifts);
    }

    @GetMapping("/week")
    public ShiftWeekResponse getWeekShifts(@AuthId final String memberId) {
        return shiftService.getWeekShifts(memberId);
    }
}
