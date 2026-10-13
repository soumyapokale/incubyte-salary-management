package com.incubyte.salary_management.controller;

import com.incubyte.salary_management.entity.Employee;
import com.incubyte.salary_management.service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public Page<Employee> searchEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(1, Math.min(size, 100)),
                Sort.by("id").ascending()
        );

        return employeeService.searchEmployees(
                search, country, department, pageable
        );
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}/salary")
    public ResponseEntity<Employee> updateSalary(
            @PathVariable Long id,
            @Valid @RequestBody SalaryUpdateRequest request) {

        Employee updatedEmployee = employeeService.updateSalary(
                id,
                request.salary(),
                request.currency(),
                request.effectiveDate()
        );

        return ResponseEntity.ok(updatedEmployee);
    }

    public record SalaryUpdateRequest(
            @NotNull
            @DecimalMin(value = "0.00")
            BigDecimal salary,

            @NotBlank
            @Pattern(regexp = "[A-Z]{3}")
            String currency,

            @NotNull
            LocalDate effectiveDate
    ) {
    }
}
