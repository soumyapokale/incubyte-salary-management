
        package com.incubyte.salary_management.service;

import com.incubyte.salary_management.entity.Employee;
import com.incubyte.salary_management.exception.EmployeeNotFoundException;
import com.incubyte.salary_management.repository.EmployeeRepository;
import com.incubyte.salary_management.service.impl.EmployeeServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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

    private EmployeeService employeeService;
    @Test
    void shouldRejectNullSalary() {
        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.updateSalary(
                        1L, null, "INR", LocalDate.of(2026, 10, 1)
                )
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void shouldRejectNullCurrency() {
        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.updateSalary(
                        1L, new BigDecimal("90000"), null,
                        LocalDate.of(2026, 10, 1)
                )
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void shouldAcceptZeroSalary() {
        Employee employee = createEmployee();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.updateSalary(
                eq(1L), eq(BigDecimal.ZERO), eq("INR"),
                eq(LocalDate.of(2026, 10, 1))
        )).thenReturn(1);

        Employee updated = employeeService.updateSalary(
                1L, BigDecimal.ZERO, "INR",
                LocalDate.of(2026, 10, 1)
        );

        assertEquals(BigDecimal.ZERO, updated.getSalary());
        verify(employeeRepository).updateSalary(
                1L, BigDecimal.ZERO, "INR", LocalDate.of(2026, 10, 1)
        );
    }

    @Test
    void shouldThrowWhenSalaryUpdateAffectsNoRows() {
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(createEmployee()));

        when(employeeRepository.updateSalary(
                anyLong(), any(BigDecimal.class), anyString(), any(LocalDate.class)
        )).thenReturn(0);

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateSalary(
                        1L, new BigDecimal("95000"), "INR",
                        LocalDate.of(2026, 10, 1)
                )
        );

        verify(employeeRepository).updateSalary(
                1L, new BigDecimal("95000"), "INR",
                LocalDate.of(2026, 10, 1)
        );
    }

    @Test
    void shouldReturnEmployeeWhenFound() {
        Employee employee = createEmployee();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(1L);

        assertSame(employee, result);
        verify(employeeRepository).findById(1L);
    }
    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    // Test 1: Negative salary should be rejected.
    @Test
    void shouldRejectNegativeSalary() {
        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.updateSalary(
                        1L,
                        new BigDecimal("-100"),
                        "INR",
                        LocalDate.of(2026, 10, 1)
                )
        );

        verifyNoInteractions(employeeRepository);
    }

    // Test 2: Valid salary update should succeed.
    @Test
    void shouldUpdateSalarySuccessfully() {
        Employee employee = createEmployee();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.updateSalary(
                1L,
                new BigDecimal("90000.00"),
                "INR",
                LocalDate.of(2026, 10, 1)
        )).thenReturn(1);

        Employee result = employeeService.updateSalary(
                1L,
                new BigDecimal("90000.00"),
                "INR",
                LocalDate.of(2026, 10, 1)
        );

        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());
        assertEquals(
                new BigDecimal("90000.00"),
                result.getSalary()
        );
        assertEquals("INR", result.getCurrency());
        assertEquals(
                LocalDate.of(2026, 10, 1),
                result.getEffectiveDate()
        );

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).updateSalary(
                1L,
                new BigDecimal("90000.00"),
                "INR",
                LocalDate.of(2026, 10, 1)
        );
    }

    // Test 3: Invalid currency should be rejected.
    @Test
    void shouldRejectInvalidCurrency() {
        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.updateSalary(
                        1L,
                        new BigDecimal("90000"),
                        "inr",
                        LocalDate.of(2026, 10, 1)
                )
        );

        verifyNoInteractions(employeeRepository);
    }

    // Test 4: Updating a nonexistent employee should fail.
    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateSalary(
                        99L,
                        new BigDecimal("90000"),
                        "INR",
                        LocalDate.of(2026, 10, 1)
                )
        );

        verify(employeeRepository).findById(99L);
        verify(employeeRepository, never()).updateSalary(
                anyLong(),
                any(BigDecimal.class),
                anyString(),
                any(LocalDate.class)
        );
    }

    // Test 5: Search should pass filters to the repository.
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

    // Test 6: Blank filters should be converted to null.
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

    // Test 7: Missing effective date should be rejected.
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

    // Helper method to create a sample employee.
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
