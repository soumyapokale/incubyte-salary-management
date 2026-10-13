
package com.incubyte.salary_management.controller;

import com.incubyte.salary_management.projection.CountrySalarySummary;
import com.incubyte.salary_management.projection.JobTitleSalarySummary;
import com.incubyte.salary_management.repository.EmployeeRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/insights")
public class SalaryInsightsController {

    private final EmployeeRepository employeeRepository;

    public SalaryInsightsController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/country-salaries")
    public List<CountrySalarySummary> getCountrySalarySummary() {
        return employeeRepository.getCountrySalarySummary();
    }

    @GetMapping("/job-title-salaries")
    public List<JobTitleSalarySummary> getJobTitleSalarySummary(
            @RequestParam String country) {
        return employeeRepository.getJobTitleSalarySummary(country);
    }
}
