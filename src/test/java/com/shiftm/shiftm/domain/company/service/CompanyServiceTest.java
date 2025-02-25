package com.shiftm.shiftm.domain.company.service;

import com.shiftm.shiftm.domain.company.domain.Company;
import com.shiftm.shiftm.domain.company.domain.CompanyBuilder;
import com.shiftm.shiftm.domain.company.dto.CompanyRequestBuilder;
import com.shiftm.shiftm.domain.company.dto.request.CompanyRequest;
import com.shiftm.shiftm.domain.company.exception.DuplicatedCompanyIdException;
import com.shiftm.shiftm.domain.company.repository.CompanyRepository;
import com.shiftm.shiftm.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CompanyServiceTest extends UnitTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    private Company company;

    @BeforeEach
    public void setUp() throws Exception {
        company = CompanyBuilder.build();
    }

    @DisplayName("회사 생성 성공")
    @Test
    void 회사_생성_성공() {
        //given
        final CompanyRequest requestDto = CompanyRequestBuilder.build();

        when(companyRepository.existsByCompanyId(any())).thenReturn(false);
        when(companyRepository.save(any())).thenReturn(company);

        //when
        final Company createCompany = companyService.createCompany(requestDto);

        //then
        assertThat(createCompany).isNotNull();
        assertThat(createCompany.getCompanyId()).isEqualTo(company.getCompanyId());
        assertThat(createCompany.getCheckinTime()).isEqualTo(company.getCheckinTime());
        assertThat(createCompany.getCheckoutTime()).isEqualTo(company.getCheckoutTime());
        assertThat(createCompany.getBreakStartTime()).isEqualTo(company.getBreakStartTime());
        assertThat(createCompany.getBreakEndTime()).isEqualTo(company.getBreakEndTime());
        assertThat(createCompany.getLatitude()).isEqualTo(company.getLatitude());
        assertThat(createCompany.getLongitude()).isEqualTo(company.getLongitude());
    }

    @DisplayName(("회사 생성 실패 - 이름 중복"))
    @Test
    public void 회사_생성_실패_이름_중복 () {
        //given
        final CompanyRequest requestDto = CompanyRequestBuilder.build();
        when(companyRepository.existsByCompanyId(any())).thenReturn(true);

        //when, then
        assertThrows(DuplicatedCompanyIdException.class, () -> companyService.createCompany(requestDto));
    }
}