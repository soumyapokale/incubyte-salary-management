
package com.incubyte.salary_management.controller;

import com.incubyte.salary_management.projection.CountrySalarySummary;
import com.incubyte.salary_management.projection.JobTitleSalarySummary;
import com.incubyte.salary_management.service.SalaryInsightsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/insights")
public class SalaryInsightsController {

    private final SalaryInsightsService salaryInsightsService;

    public SalaryInsightsController(
            SalaryInsightsService salaryInsightsService) {
        this.salaryInsightsService = salaryInsightsService;
    }

    // GET /api/insights/country-salaries
    @GetMapping("/country-salaries")
    public List<CountrySalarySummary> getCountrySalarySummary() {
        return salaryInsightsService.getCountrySalarySummary();
    }

    // GET /api/insights/job-title-salaries?country=India
    @GetMapping("/job-title-salaries")
    public List<JobTitleSalarySummary> getJobTitleSalarySummary(
            @RequestParam String country) {
        return salaryInsightsService.getJobTitleSalarySummary(country);
    }
}
