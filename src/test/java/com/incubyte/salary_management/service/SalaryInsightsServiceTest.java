
package com.incubyte.salary_management.service;

import com.incubyte.salary_management.projection.CountrySalarySummary;
import com.incubyte.salary_management.projection.JobTitleSalarySummary;
import com.incubyte.salary_management.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryInsightsServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private SalaryInsightsService salaryInsightsService;

    @Test
    void getCountrySalarySummaryReturnsResults() {
        CountrySalarySummary summary = mock(CountrySalarySummary.class);

        when(employeeRepository.getCountrySalarySummary())
                .thenReturn(List.of(summary));

        List<CountrySalarySummary> result =
                salaryInsightsService.getCountrySalarySummary();

        assertEquals(1, result.size());
        assertSame(summary, result.get(0));

        verify(employeeRepository).getCountrySalarySummary();
    }

    @Test
    void getCountrySalarySummaryReturnsEmptyList() {
        when(employeeRepository.getCountrySalarySummary())
                .thenReturn(List.of());

        List<CountrySalarySummary> result =
                salaryInsightsService.getCountrySalarySummary();

        assertTrue(result.isEmpty());

        verify(employeeRepository).getCountrySalarySummary();
    }

    @Test
    void getJobTitleSalarySummaryReturnsResultsForCountry() {
        JobTitleSalarySummary summary = mock(JobTitleSalarySummary.class);

        when(employeeRepository.getJobTitleSalarySummary("India"))
                .thenReturn(List.of(summary));

        List<JobTitleSalarySummary> result =
                salaryInsightsService.getJobTitleSalarySummary(" India ");

        assertEquals(1, result.size());
        assertSame(summary, result.get(0));

        verify(employeeRepository).getJobTitleSalarySummary("India");
    }

    @Test
    void getJobTitleSalarySummaryReturnsEmptyList() {
        when(employeeRepository.getJobTitleSalarySummary("Japan"))
                .thenReturn(List.of());

        List<JobTitleSalarySummary> result =
                salaryInsightsService.getJobTitleSalarySummary("Japan");

        assertTrue(result.isEmpty());

        verify(employeeRepository).getJobTitleSalarySummary("Japan");
    }

    @Test
    void getJobTitleSalarySummaryRejectsNullCountry() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> salaryInsightsService.getJobTitleSalarySummary(null)
        );

        assertEquals("Country is required", exception.getMessage());

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void getJobTitleSalarySummaryRejectsBlankCountry() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> salaryInsightsService.getJobTitleSalarySummary("   ")
        );

        assertEquals("Country is required", exception.getMessage());

        verifyNoInteractions(employeeRepository);
    }
}
