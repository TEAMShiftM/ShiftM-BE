package com.shiftm.shiftm.domain.shift.api;

import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.dto.request.CheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.response.CheckinResponse;
import com.shiftm.shiftm.domain.shift.service.ShiftService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
