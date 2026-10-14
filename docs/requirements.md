# Incubyte Salary Management — Requirements

## Goal
Provide a simple web application that lets an HR Manager manage current employee salary data for an organisation of 10,000 employees and answer basic questions about salary distribution across countries and job titles.

## User and assumptions
- **Primary user:** one HR Manager.
- The application is an assessment prototype using fictional employee data.
- The organisation has 10,000 employees across five countries, with 2,000 seeded employees per country.
- Each employee has one current salary, represented by an amount and an ISO-style three-letter currency code, plus an effective date.
- Salary insights are calculated within each country and its currency. Salaries in different currencies are never added together or compared as if they were the same currency.
- The application assumes a single HR Manager and does not require login, roles, or audit tracking for this assessment.

## In scope
1. Seed the database with 10,000 fictional employee records.
2. Browse the employee directory, view employee details, and search/filter by supported employee attributes.
3. Use server-side pagination to keep directory responses manageable.
4. Update an employee's current salary, currency, and effective date.
5. Display employee counts by country and department.
6. Display, for each country, the minimum, maximum, and average salary in that country's currency.
7. Display average salary by job title within a selected country.
8. Provide a web UI, backend API, relational database, and automated tests for core behaviour.

## Deliberately out of scope
- **Salary history:** only the current salary is stored. If historical reporting becomes a requirement, add a salary-history table linked to the employee, recording salary amount, currency, effective date, end date (or current-record marker), and change metadata. The current salary could then be derived from the active record or maintained transactionally with history.
- **Authentication and authorisation:** no login, roles, or permissions are implemented. A real deployment should use the organisation's identity provider and enforce server-side access control.
- **Audit trail:** salary changes are not recorded in a separate audit log. A production version should record who changed what and when, with before/after values and a correlation/request identifier.
- **Currency conversion and consolidated totals:** country insights stay in the country's own currency. No exchange-rate source, conversion date, or consolidated cross-country total is required.
- Payroll processing, tax/deduction calculations, payslips, bank transfers, employee self-service, notifications, and complex historical analytics.

## Success criteria
- A clean setup can seed 10,000 fictional employees.
- HR can search/filter and page through employees without loading all records at once.
- A valid salary update is persisted and visible after reloading the employee.
- Country-level minimum, maximum, and average salary are available in local currency.
- Average salary by job title is available for a selected country.
- Automated tests cover core service/API behaviour and important validation/error cases.
- Setup instructions, design decisions, and known limitations are documented.
