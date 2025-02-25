package com.shiftm.shiftm.domain.company.api;

import com.shiftm.shiftm.domain.company.domain.Company;
import com.shiftm.shiftm.domain.company.dto.request.CompanyRequest;
import com.shiftm.shiftm.domain.company.dto.response.CompanyResponse;
import com.shiftm.shiftm.domain.company.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin/company")
@RestController
public class AdminCompanyController {

    private final CompanyService companyService;

    @PostMapping
    public CompanyResponse createCompany(@Valid @RequestBody final CompanyRequest requestDto) {
        final Company company = companyService.createCompany(requestDto);
        return new CompanyResponse(company);
    }
}
