package com.incubyte.salary_management.repository;

import com.incubyte.salary_management.constants.EmployeeSql;
import com.incubyte.salary_management.entity.Employee;

import com.incubyte.salary_management.projection.CountrySalarySummary;
import com.incubyte.salary_management.projection.JobTitleSalarySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    @Query(value = EmployeeSql.COUNTRY_SALARY_SUMMARY, nativeQuery = true)
    List<CountrySalarySummary> getCountrySalarySummary();

    @Query(value = EmployeeSql.JOB_TITLE_SALARY_SUMMARY, nativeQuery = true)
    List<JobTitleSalarySummary> getJobTitleSalarySummary(
            @Param("country") String country
    );

    @Query(
            value = EmployeeSql.SEARCH_EMPLOYEES,
            countQuery = EmployeeSql.COUNT_EMPLOYEES,
            nativeQuery = true
    )
    Page<Employee> searchEmployees(
            @Param("search") String search,
            @Param("country") String country,
            @Param("department") String department,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(
            value = EmployeeSql.UPDATE_SALARY,
            nativeQuery = true
    )
    int updateSalary(
            @Param("id") Long id,
            @Param("salary") BigDecimal salary,
            @Param("currency") String currency,
            @Param("effectiveDate") LocalDate effectiveDate
    );
}
