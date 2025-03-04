package com.shiftm.shiftm.domain.shift.dto.request;

import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import jakarta.validation.constraints.NotNull;

public record ShiftStatusRequest(
        @NotNull
        Status status
) {
}
