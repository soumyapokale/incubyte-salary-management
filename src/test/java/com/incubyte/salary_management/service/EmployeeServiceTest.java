
package com.incubyte.salary_management.service;

import com.incubyte.salary_management.entity.Employee;
import com.incubyte.salary_management.exception.EmployeeNotFoundException;
import com.incubyte.salary_management.repository.EmployeeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    // Test 1: Negative salary should be rejected
    @Test
    void shouldRejectNegativeSalary() {

        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.updateSalary(
                        1L,
                        new BigDecimal("-100"),
                        "INR",
                        LocalDate.now()
                )
        );

        verifyNoInteractions(employeeRepository);
    }

    // Test 2: Valid salary update should succeed
    @Test
    void shouldUpdateSalarySuccessfully() {

        Employee employee = createEmployee();

        when(employeeRepository.updateSalary(
                1L,
                new BigDecimal("90000.00"),
                "INR",
                LocalDate.of(2026, 10, 1)
        )).thenReturn(1);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        Employee result = employeeService.updateSalary(
                1L,
                new BigDecimal("90000.00"),
                "INR",
                LocalDate.of(2026, 10, 1)
        );

        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());

        verify(employeeRepository).updateSalary(
                1L,
                new BigDecimal("90000.00"),
                "INR",
                LocalDate.of(2026, 10, 1)
        );
    }

    // Test 3: Invalid currency should be rejected
    @Test
    void shouldRejectInvalidCurrency() {

        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.updateSalary(
                        1L,
                        new BigDecimal("90000"),
                        "inr",
                        LocalDate.now()
                )
        );

        verifyNoInteractions(employeeRepository);
    }

    // Test 4: Updating a nonexistent employee should fail
    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {

        when(employeeRepository.updateSalary(
                99L,
                new BigDecimal("90000"),
                "INR",
                LocalDate.now()
        )).thenReturn(0);

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateSalary(
                        99L,
                        new BigDecimal("90000"),
                        "INR",
                        LocalDate.now()
                )
        );

        verify(employeeRepository).updateSalary(
                99L,
                new BigDecimal("90000"),
                "INR",
                LocalDate.now()
        );
    }

    // Test 5: Search should pass filters to the repository
    @Test
    void shouldSearchEmployeesUsingProvidedFilters() {

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Employee> expectedPage =
                new PageImpl<>(List.of(createEmployee()));

        when(employeeRepository.searchEmployees(
                "Aarav",
                "India",
                "Engineering",
                pageable
        )).thenReturn(expectedPage);

        Page<Employee> result = employeeService.searchEmployees(
                "Aarav",
                "India",
                "Engineering",
                pageable
        );

        assertEquals(1, result.getTotalElements());
        assertEquals(
                "EMP001",
                result.getContent().get(0).getEmployeeCode()
        );

        verify(employeeRepository).searchEmployees(
                "Aarav",
                "India",
                "Engineering",
                pageable
        );
    }

    // Test 6: Blank filters should be converted to null
    @Test
    void shouldConvertBlankFiltersToNull() {

        PageRequest pageable = PageRequest.of(0, 10);

        when(employeeRepository.searchEmployees(
                null,
                null,
                null,
                pageable
        )).thenReturn(new PageImpl<>(List.of()));

        Page<Employee> result = employeeService.searchEmployees(
                "   ",
                "",
                null,
                pageable
        );

        assertTrue(result.isEmpty());

        verify(employeeRepository).searchEmployees(
                null,
                null,
                null,
                pageable
        );
    }

    // Test 7: Missing effective date should be rejected
    @Test
    void shouldRejectMissingEffectiveDate() {

        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.updateSalary(
                        1L,
                        new BigDecimal("90000"),
                        "INR",
                        null
                )
        );

        verifyNoInteractions(employeeRepository);
    }

    // Helper method to create a sample employee
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
        employee.setSalary(new BigDecimal("90000.00"));
        employee.setCurrency("INR");
        employee.setEffectiveDate(LocalDate.of(2026, 10, 1));

        return employee;
    }
}
