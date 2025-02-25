package com.shiftm.shiftm.domain.company.repository;

import com.shiftm.shiftm.domain.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCompanyId(String companyId);
}
