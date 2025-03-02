package com.shiftm.shiftm.domain.company.service;

import com.shiftm.shiftm.domain.company.domain.Company;
import com.shiftm.shiftm.domain.company.domain.CompanyBuilder;
import com.shiftm.shiftm.domain.company.dto.CompanyRequestBuilder;
import com.shiftm.shiftm.domain.company.dto.request.CompanyRequest;
import com.shiftm.shiftm.domain.company.exception.CompanyNotFoundException;
import com.shiftm.shiftm.domain.company.exception.CompanyAlreadyExistsException;
import com.shiftm.shiftm.domain.company.repository.CompanyRepository;
import com.shiftm.shiftm.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalTime;
import java.util.Optional;

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
        // given
        final CompanyRequest requestDto = CompanyRequestBuilder.build();

        when(companyRepository.exists()).thenReturn(false);
        when(companyRepository.save(any())).thenReturn(company);

        // when
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

    @DisplayName(("회사 생성 실패 - 이미 생성한 회사"))
    @Test
    public void 회사_생성_실패_이미_존재 () {
        // given
        final CompanyRequest requestDto = CompanyRequestBuilder.build();

        when(companyRepository.exists()).thenReturn(true);

        // when, then
        assertThrows(CompanyAlreadyExistsException.class, () -> companyService.createCompany(requestDto));
    }

    @DisplayName("회사 수정 성공")
    @Test
    void 회사_수정_성공() {
        // given
        final CompanyRequest requestDto = new CompanyRequest("shift", LocalTime.of(0, 0), LocalTime.of(0, 0),
                LocalTime.of(0, 0), LocalTime.of(0, 0), 0.0, 0.0);

        when(companyRepository.findById(any())).thenReturn(Optional.of(company));

        // when
        final Company updateCompany = companyService.updateCompany(null, requestDto);

        // then
        assertThat(updateCompany).isNotNull();
        assertThat(updateCompany.getCompanyId()).isEqualTo("shift");
        assertThat(updateCompany.getCheckinTime()).isEqualTo(LocalTime.of(0, 0));
        assertThat(updateCompany.getCheckoutTime()).isEqualTo(LocalTime.of(0, 0));
        assertThat(updateCompany.getBreakStartTime()).isEqualTo(LocalTime.of(0, 0));
        assertThat(updateCompany.getBreakEndTime()).isEqualTo(LocalTime.of(0, 0));
        assertThat(updateCompany.getLatitude()).isEqualTo(0.0);
        assertThat(updateCompany.getLongitude()).isEqualTo(0.0);
    }

    @DisplayName("회사 수정 실패 - 존재하지 않는 회사ID")
    @Test
    public void 회사_수정_실패_존재하지_않는_회사ID() {
        // given
        final CompanyRequest requestDto = new CompanyRequest("shift", LocalTime.of(0, 0), LocalTime.of(0, 0),
                LocalTime.of(0, 0), LocalTime.of(0, 0), 0.0, 0.0);

        when(companyRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompanyNotFoundException.class, () -> companyService.updateCompany(100L, requestDto));
    }

    @DisplayName("회사 조회 성공")
    @Test
    public void 회사_조회_성공() {
        // given
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));

        // when
        final Company getCompany = companyService.getCompany(null);

        // then
        assertThat(getCompany).isNotNull();
        assertThat(getCompany.getCompanyId()).isEqualTo(company.getCompanyId());
        assertThat(getCompany.getCheckinTime()).isEqualTo(company.getCheckinTime());
        assertThat(getCompany.getCheckoutTime()).isEqualTo(company.getCheckoutTime());
        assertThat(getCompany.getBreakStartTime()).isEqualTo(company.getBreakStartTime());
        assertThat(getCompany.getBreakEndTime()).isEqualTo(company.getBreakEndTime());
        assertThat(getCompany.getLatitude()).isEqualTo(company.getLatitude());
        assertThat(getCompany.getLongitude()).isEqualTo(company.getLongitude());
    }

    @DisplayName("회사 조회 실패 - 존재하지 않는 회사ID")
    @Test
    public void 회사_조회_실패_존재하지_않는_회사ID() {
        // given
        when(companyRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThrows(CompanyNotFoundException.class, () -> companyService.getCompany(null));
    }
}