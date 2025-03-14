package com.shiftm.shiftm.domain.company.api;

import com.shiftm.shiftm.domain.company.dto.response.CompanyResponse;
import com.shiftm.shiftm.domain.company.repository.CompanyFindDao;
import com.shiftm.shiftm.domain.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/company")
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public CompanyResponse getCompany() {
        return new CompanyResponse(companyService.getFirstCompany());
    }
}
