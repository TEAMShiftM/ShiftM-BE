package com.shiftm.shiftm.domain.company.dto.response;

import com.shiftm.shiftm.domain.company.domain.Company;

import java.time.LocalTime;

public record CompanyResponse(
        String companyId,
        LocalTime checkinTime,
        LocalTime checkoutTime,
        LocalTime breakStartTime,
        LocalTime breakEndTime,
        Double latitude,
        Double longitude
)
{
    public CompanyResponse(final Company company) {
        this(company.getCompanyId(), company.getCheckinTime(), company.getCheckoutTime(),
                company.getBreakStartTime(), company.getBreakEndTime(), company.getLatitude(), company.getLongitude());
    }
}
