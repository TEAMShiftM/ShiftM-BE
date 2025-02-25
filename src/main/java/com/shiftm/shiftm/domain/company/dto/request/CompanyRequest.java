package com.shiftm.shiftm.domain.company.dto.request;

import com.shiftm.shiftm.domain.company.domain.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record CompanyRequest(
        @NotBlank
        String companyId,
        @NotNull
        LocalTime checkinTime,
        @NotNull
        LocalTime checkoutTime,
        @NotNull
        LocalTime breakStartTime,
        @NotNull
        LocalTime breakEndTime,
        @NotNull
        Double latitude,
        @NotNull
        Double longitude
) {
    public Company toEntity(final String companyId, final LocalTime checkinTime, final LocalTime checkoutTime,
                            final LocalTime breakStartTime, final LocalTime breakEndTime, final Double latitude, final Double longitude) {
        return Company.builder()
                .companyId(companyId)
                .checkinTime(checkinTime)
                .checkoutTime(checkoutTime)
                .breakStartTime(breakStartTime)
                .breakEndTime(breakEndTime)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
