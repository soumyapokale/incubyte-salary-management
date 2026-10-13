package com.incubyte.salary_management.constants;

public final class EmployeeSql {

    private EmployeeSql() {
    }

    public static final String COUNTRY_SALARY_SUMMARY = """
    SELECT
        country AS country,
        currency AS currency,
        MIN(salary) AS "minSalary",
        MAX(salary) AS "maxSalary",
        ROUND(AVG(salary), 2) AS "averageSalary",
        COUNT(*) AS "employeeCount"
    FROM employees
    GROUP BY country, currency
    ORDER BY country
    """;

    public static final String JOB_TITLE_SALARY_SUMMARY = """
    SELECT
        country AS country,
        currency AS currency,
        job_title AS "jobTitle",
        ROUND(AVG(salary), 2) AS "averageSalary",
        COUNT(*) AS "employeeCount"
    FROM employees
    WHERE LOWER(country) = LOWER(:country)
    GROUP BY country, currency, job_title
    ORDER BY job_title
    """;

    public static final String SEARCH_EMPLOYEES = """
            SELECT *
            FROM employees e
            WHERE
                (
                    CAST(:search AS VARCHAR) IS NULL
                    OR LOWER(e.employee_code)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(e.first_name)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(e.last_name)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(e.email)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND (
                    CAST(:country AS VARCHAR) IS NULL
                    OR LOWER(e.country) = LOWER(:country)
                )
                AND (
                    CAST(:department AS VARCHAR) IS NULL
                    OR LOWER(e.department) = LOWER(:department)
                )
            ORDER BY e.id
            """;

    public static final String COUNT_EMPLOYEES = """
            SELECT COUNT(*)
            FROM employees e
            WHERE
                (
                    CAST(:search AS VARCHAR) IS NULL
                    OR LOWER(e.employee_code)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(e.first_name)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(e.last_name)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(e.email)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND (
                    CAST(:country AS VARCHAR) IS NULL
                    OR LOWER(e.country) = LOWER(:country)
                )
                AND (
                    CAST(:department AS VARCHAR) IS NULL
                    OR LOWER(e.department) = LOWER(:department)
                )
            """;

    public static final String UPDATE_SALARY = """
            UPDATE employees
            SET salary = :salary,
                currency = :currency,
                effective_date = :effectiveDate
            WHERE id = :id
            """;
}
