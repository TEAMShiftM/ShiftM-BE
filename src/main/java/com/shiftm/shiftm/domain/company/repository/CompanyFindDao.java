package com.shiftm.shiftm.domain.company.repository;

import com.shiftm.shiftm.domain.company.domain.Company;
import com.shiftm.shiftm.domain.company.exception.CompanyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyFindDao {

    private final CompanyRepository companyRepository;

    public Company findById(final Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException());
    }

    public Company findFirst() {
        return companyRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new CompanyNotFoundException());
    }
}
