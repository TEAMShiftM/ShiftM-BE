package com.shiftm.shiftm.domain.company.api;

import com.shiftm.shiftm.domain.company.domain.Company;
import com.shiftm.shiftm.domain.company.dto.request.CompanyRequest;
import com.shiftm.shiftm.domain.company.dto.response.CompanyResponse;
import com.shiftm.shiftm.domain.company.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{companyId}")
    public CompanyResponse updateCompany(@PathVariable(name = "companyId") final Long companyId,
                                         @Valid @RequestBody final CompanyRequest requestDto) {
        final Company company = companyService.updateCompany(companyId, requestDto);
        return new CompanyResponse(company);
    }
}
