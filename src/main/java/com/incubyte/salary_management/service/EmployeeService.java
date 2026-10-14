
        package com.incubyte.salary_management.service;

import com.incubyte.salary_management.DTO.EmployeeCountsResponse;
import com.incubyte.salary_management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface EmployeeService {

    Page<Employee> searchEmployees(
            String search,
            String country,
            String department,
            Pageable pageable
    );

    EmployeeCountsResponse getEmployeeCounts();

    Employee getEmployeeById(Long id);

    Employee updateSalary(
            Long id,
            BigDecimal salary,
            String currency,
            LocalDate effectiveDate
    );
}
