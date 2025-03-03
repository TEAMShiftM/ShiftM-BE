package com.shiftm.shiftm.domain.company.dto;

import com.shiftm.shiftm.domain.company.dto.request.CompanyRequest;

import java.time.LocalTime;

public class CompanyRequestBuilder {

    public static CompanyRequest build() {
        final String companyId = "shiftm";
        final LocalTime checkinTime = LocalTime.of(9, 0);
        final LocalTime chekoutTime = LocalTime.of(18, 0);
        final LocalTime breakStartTime = LocalTime.of(12, 0);
        final LocalTime breakEndTIme = LocalTime.of(13, 0);
        final Double latitude = 126.734086;
        final Double logitude = 37.566535;

        return new CompanyRequest(companyId, checkinTime, chekoutTime, breakStartTime, breakEndTIme, latitude, logitude);
    }
}
