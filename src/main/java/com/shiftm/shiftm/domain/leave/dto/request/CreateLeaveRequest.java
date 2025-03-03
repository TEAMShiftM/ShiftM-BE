package com.shiftm.shiftm.domain.leave.dto.request;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreateLeaveRequest(
        @NotNull
        @Size(min = 1)
        List<String> memberIds,
        @NotNull
        Long leaveTypeId,
        @NotNull
        LocalDate expirationDate,
        @NotNull
        @Min(value = 1)
        Integer count
) {
    public Leave toEntity() {
        return Leave.builder()
                .count(count)
                .expirationDate(expirationDate)
                .build();
    }
}