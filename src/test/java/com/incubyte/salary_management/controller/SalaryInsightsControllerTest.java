
package com.incubyte.salary_management.controller;

import com.incubyte.salary_management.projection.CountrySalarySummary;
import com.incubyte.salary_management.projection.JobTitleSalarySummary;
import com.incubyte.salary_management.service.SalaryInsightsService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalaryInsightsController.class)
class SalaryInsightsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalaryInsightsService salaryInsightsService;

    // Concrete test data avoids serializing Mockito internals.
    private record CountrySummary(
            String country,
            String currency,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            BigDecimal averageSalary,
            Long employeeCount
    ) implements CountrySalarySummary {

        @Override
        public String getCountry() {
            return country;
        }

        @Override
        public String getCurrency() {
            return currency;
        }

        @Override
        public BigDecimal getMinSalary() {
            return minSalary;
        }

        @Override
        public BigDecimal getMaxSalary() {
            return maxSalary;
        }

        @Override
        public BigDecimal getAverageSalary() {
            return averageSalary;
        }

        @Override
        public Long getEmployeeCount() {
            return employeeCount;
        }
    }

    private record JobTitleSummary(
            String country,
            String currency,
            String jobTitle,
            BigDecimal averageSalary,
            Long employeeCount
    ) implements JobTitleSalarySummary {

        @Override
        public String getCountry() {
            return country;
        }

        @Override
        public String getCurrency() {
            return currency;
        }

        @Override
        public String getJobTitle() {
            return jobTitle;
        }

        @Override
        public BigDecimal getAverageSalary() {
            return averageSalary;
        }

        @Override
        public Long getEmployeeCount() {
            return employeeCount;
        }
    }

    @Test
    void getCountrySalarySummaryReturnsEmptyList() throws Exception {
        when(salaryInsightsService.getCountrySalarySummary())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/insights/country-salaries"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(salaryInsightsService).getCountrySalarySummary();
    }

    @Test
    void getCountrySalarySummaryReturnsData() throws Exception {
        CountrySalarySummary summary = new CountrySummary(
                "India",
                "INR",
                new BigDecimal("45000.00"),
                new BigDecimal("150000.00"),
                new BigDecimal("95000.00"),
                2000L
        );

        when(salaryInsightsService.getCountrySalarySummary())
                .thenReturn(List.of(summary));

        mockMvc.perform(get("/api/insights/country-salaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country").value("India"))
                .andExpect(jsonPath("$[0].currency").value("INR"))
                .andExpect(jsonPath("$[0].minSalary").value(45000.00))
                .andExpect(jsonPath("$[0].maxSalary").value(150000.00))
                .andExpect(jsonPath("$[0].averageSalary").value(95000.00))
                .andExpect(jsonPath("$[0].employeeCount").value(2000));

        verify(salaryInsightsService).getCountrySalarySummary();
    }

    @Test
    void getJobTitleSalarySummaryReturnsEmptyList() throws Exception {
        when(salaryInsightsService.getJobTitleSalarySummary("India"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/insights/job-title-salaries")
                        .param("country", "India"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(salaryInsightsService)
                .getJobTitleSalarySummary("India");
    }

    @Test
    void getJobTitleSalarySummaryReturnsData() throws Exception {
        JobTitleSalarySummary summary = new JobTitleSummary(
                "India",
                "INR",
                "Software Engineer",
                new BigDecimal("95000.00"),
                400L
        );

        when(salaryInsightsService.getJobTitleSalarySummary("India"))
                .thenReturn(List.of(summary));

        mockMvc.perform(get("/api/insights/job-title-salaries")
                        .param("country", "India"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country").value("India"))
                .andExpect(jsonPath("$[0].currency").value("INR"))
                .andExpect(jsonPath("$[0].jobTitle")
                        .value("Software Engineer"))
                .andExpect(jsonPath("$[0].averageSalary").value(95000.00))
                .andExpect(jsonPath("$[0].employeeCount").value(400));

        verify(salaryInsightsService)
                .getJobTitleSalarySummary("India");
    }

    @Test
    void getJobTitleSalarySummaryWithoutCountryReturnsBadRequest()
            throws Exception {

        mockMvc.perform(get("/api/insights/job-title-salaries"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Required parameter is missing: country"));

        verifyNoInteractions(salaryInsightsService);
    }
}
