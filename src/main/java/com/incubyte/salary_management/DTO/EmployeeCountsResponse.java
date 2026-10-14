package com.incubyte.salary_management.DTO;

import com.incubyte.salary_management.projection.CountryEmployeeCount;
import com.incubyte.salary_management.projection.DepartmentEmployeeCount;

import java.util.List;

public record EmployeeCountsResponse(
        List<CountryEmployeeCount> byCountry,
        List<DepartmentEmployeeCount> byDepartment
) {
}
