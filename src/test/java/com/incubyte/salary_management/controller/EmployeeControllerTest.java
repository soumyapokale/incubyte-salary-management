
package com.incubyte.salary_management.controller;

import com.incubyte.salary_management.entity.Employee;
import com.incubyte.salary_management.service.EmployeeService;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    // Test 1: GET employees returns a paginated response
    @Test
    void shouldReturnEmployees() throws Exception {

        Employee employee = createEmployee();

        Page<Employee> page =
                new PageImpl<>(List.of(employee), PageRequest.of(0, 10), 1);

        when(employeeService.searchEmployees(
                isNull(),
                isNull(),
                isNull(),
                any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].employeeCode").value("EMP001"))
                .andExpect(jsonPath("$.content[0].country").value("India"));
    }

    // Test 2: Search and filters are passed to the service
    @Test
    void shouldPassSearchAndFiltersToService() throws Exception {

        when(employeeService.searchEmployees(
                eq("Aarav"),
                eq("India"),
                eq("Engineering"),
                any()
        )).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/employees")
                        .param("search", "Aarav")
                        .param("country", "India")
                        .param("department", "Engineering"))
                .andExpect(status().isOk());

        verify(employeeService).searchEmployees(
                eq("Aarav"),
                eq("India"),
                eq("Engineering"),
                any()
        );
    }

    // Test 3: GET employee by ID returns the employee
    @Test
    void shouldReturnEmployeeById() throws Exception {

        when(employeeService.getEmployeeById(1L))
                .thenReturn(createEmployee());

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode").value("EMP001"))
                .andExpect(jsonPath("$.salary").value(85000));
    }

    // Test 4: PUT salary endpoint accepts a valid request
    @Test
    void shouldUpdateEmployeeSalary() throws Exception {

        Employee employee = createEmployee();
        employee.setSalary(new BigDecimal("90000.00"));

        when(employeeService.updateSalary(
                eq(1L),
                eq(new BigDecimal("90000.00")),
                eq("INR"),
                eq(LocalDate.of(2026, 10, 1))
        )).thenReturn(employee);

        mockMvc.perform(put("/api/employees/1/salary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "salary": 90000.00,
                                  "currency": "INR",
                                  "effectiveDate": "2026-10-01"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary").value(90000));
    }

    // Test 5: Invalid currency is rejected by request validation
    @Test
    void shouldRejectInvalidCurrency() throws Exception {

        mockMvc.perform(put("/api/employees/1/salary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "salary": 90000.00,
                                  "currency": "inr",
                                  "effectiveDate": "2026-10-01"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(employeeService);
    }

    private Employee createEmployee() {

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setEmployeeCode("EMP001");
        employee.setFirstName("Aarav");
        employee.setLastName("Sharma");
        employee.setEmail("aarav.sharma@example.com");
        employee.setDepartment("Engineering");
        employee.setCountry("India");
        employee.setJobTitle("Software Engineer");
        employee.setSalary(new BigDecimal("85000.00"));
        employee.setCurrency("INR");
        employee.setEffectiveDate(LocalDate.of(2026, 10, 1));

        return employee;
    }
}
