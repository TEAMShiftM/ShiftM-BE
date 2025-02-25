package com.shiftm.shiftm.domain.company.service;

import com.shiftm.shiftm.domain.company.domain.Company;
import com.shiftm.shiftm.domain.company.dto.request.CompanyRequest;
import com.shiftm.shiftm.domain.company.exception.DuplicatedCompanyIdException;
import com.shiftm.shiftm.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public Company createCompany(final CompanyRequest requestDto) {
        validateCompanyId(requestDto.companyId());
        final Company company = requestDto.toEntity(requestDto.companyId(), requestDto.checkinTime(), requestDto.checkoutTime(),
                requestDto.breakStartTime(), requestDto.breakEndTime(), requestDto.latitude(), requestDto.longitude());
        return companyRepository.save(company);
    }

    private void validateCompanyId(final String companyId) {
        if (companyRepository.existsByCompanyId(companyId)) {
            throw new DuplicatedCompanyIdException(companyId);
        }
    }
}
