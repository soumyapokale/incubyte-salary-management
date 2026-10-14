
        package com.incubyte.salary_management.service.impl;

import com.incubyte.salary_management.DTO.EmployeeCountsResponse;
import com.incubyte.salary_management.entity.Employee;
import com.incubyte.salary_management.exception.EmployeeNotFoundException;
import com.incubyte.salary_management.projection.CountryEmployeeCount;
import com.incubyte.salary_management.projection.DepartmentEmployeeCount;
import com.incubyte.salary_management.repository.EmployeeRepository;
import com.incubyte.salary_management.service.EmployeeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Page<Employee> searchEmployees(
            String search,
            String country,
            String department,
            Pageable pageable
    ) {
        return employeeRepository.searchEmployees(
                normalize(search),
                normalize(country),
                normalize(department),
                pageable
        );
    }

    @Override
    public EmployeeCountsResponse getEmployeeCounts() {
        // These repository queries already exist in EmployeeRepository.
        var countryCounts = employeeRepository.getEmployeeCountByCountry();
        var departmentCounts =
                employeeRepository.getEmployeeCountByDepartment();

        return new EmployeeCountsResponse(
                countryCounts,
                departmentCounts
        );
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found: " + id
                        )
                );
    }


    @Override
    @Transactional
    public Employee updateSalary(
            Long id,
            BigDecimal salary,
            String currency,
            LocalDate effectiveDate
    ) {
        // Validate salary before any database interaction.
        if (salary == null || salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Salary must be zero or greater"
            );
        }

        // Currency must be a three-letter uppercase code.
        if (currency == null || !currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException(
                    "Currency must be a three-letter uppercase code"
            );
        }

        // Effective date is mandatory.
        if (effectiveDate == null) {
            throw new IllegalArgumentException(
                    "Effective date is required"
            );
        }

        // Find employee only after validating all inputs.
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found: " + id
                        )
                );

        int updatedRows = employeeRepository.updateSalary(
                id,
                salary,
                currency,
                effectiveDate
        );

        if (updatedRows == 0) {
            throw new EmployeeNotFoundException(
                    "Employee not found: " + id
            );
        }

        // Keep the returned entity consistent with the database.
        employee.setSalary(salary);
        employee.setCurrency(currency);
        employee.setEffectiveDate(effectiveDate);

        return employee;
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
