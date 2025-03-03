package com.shiftm.shiftm.domain.shift.api;

import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.dto.request.AfterCheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckoutRequest;
import com.shiftm.shiftm.domain.shift.dto.response.AfterCheckinResponse;
import com.shiftm.shiftm.domain.shift.dto.response.CheckinResponse;
import com.shiftm.shiftm.domain.shift.dto.response.CheckoutResponse;
import com.shiftm.shiftm.domain.shift.dto.response.ShiftResponse;
import com.shiftm.shiftm.domain.shift.service.ShiftService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/shift")
@RestController
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping("/check-in")
    public CheckinResponse createCheckin(@AuthId String memberId, @Valid @RequestBody final CheckinRequest requestDto) {
        final Shift shift = shiftService.createCheckin(memberId, requestDto);
        return new CheckinResponse(shift);
    }

    @PostMapping("/check-in/after")
    public AfterCheckinResponse createAfterCheckin(@AuthId String memberId, @Valid @RequestBody final AfterCheckinRequest requestDto) {
        final Shift shift = shiftService.createAfterCheckin(memberId, requestDto);
        return new AfterCheckinResponse(shift);
    }

    @PatchMapping("/check-out")
    public CheckoutResponse createCheckout(@AuthId String memberId, @Valid @RequestBody final CheckoutRequest requestDto) {
        final Shift shift = shiftService.createCheckout(memberId, requestDto);
        return new CheckoutResponse(shift);
    }

    @GetMapping
    public List<ShiftResponse> getShiftsInRange(@AuthId String memberId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (startDate == null) {
            startDate = today.minusYears(100);
        }
        if (endDate == null) {
            endDate = today;
        }
        return shiftService.getShiftsInRange(memberId, startDate, endDate);
    }
}
