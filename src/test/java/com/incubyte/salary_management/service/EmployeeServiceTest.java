
package com.incubyte.salary_management.service;

import com.incubyte.salary_management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

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
}
