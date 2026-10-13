
        package com.incubyte.salary_management.service;

import com.incubyte.salary_management.entity.Employee;
import com.incubyte.salary_management.exception.EmployeeNotFoundException;
import com.incubyte.salary_management.repository.EmployeeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> searchEmployees(
            String search,
            String country,
            String department,
            Pageable pageable) {

        return employeeRepository.searchEmployees(
                blankToNull(search),
                blankToNull(country),
                blankToNull(department),
                pageable
        );
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with ID: " + id
                        )
                );
    }

    @Transactional
    public Employee updateSalary(
            Long id,
            BigDecimal salary,
            String currency,
            LocalDate effectiveDate) {

        if (salary == null || salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Salary must be greater than or equal to zero"
            );
        }

        if (currency == null || !currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException(
                    "Currency must be a 3-letter uppercase code"
            );
        }

        if (effectiveDate == null) {
            throw new IllegalArgumentException(
                    "Effective date is required"
            );
        }

        int updatedRows = employeeRepository.updateSalary(
                id, salary, currency, effectiveDate
        );

        if (updatedRows == 0) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + id
            );
        }

        return getEmployeeById(id);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}
