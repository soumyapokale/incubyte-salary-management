
package com.incubyte.salary_management.service;

import com.incubyte.salary_management.projection.CountrySalarySummary;
import com.incubyte.salary_management.projection.JobTitleSalarySummary;
import com.incubyte.salary_management.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalaryInsightsService {

    private final EmployeeRepository employeeRepository;

    public SalaryInsightsService(
            EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Minimum, maximum and average salary by country
    @Transactional(readOnly = true)
    public List<CountrySalarySummary> getCountrySalarySummary() {
        return employeeRepository.getCountrySalarySummary();
    }

    // Average salary by job title within a country
    @Transactional(readOnly = true)
    public List<JobTitleSalarySummary> getJobTitleSalarySummary(
            String country) {

        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException(
                    "Country is required"
            );
        }

        return employeeRepository.getJobTitleSalarySummary(
                country.trim()
        );
    }
}
