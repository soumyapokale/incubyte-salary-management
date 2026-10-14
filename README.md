# Incubyte Salary Management

A full-stack salary management application designed for an HR Manager to manage employee salary information and understand salary distribution across countries and job titles. The application is built for the assessment scenario of 10,000 employees, with an emphasis on maintainable code, useful insights, and focused scope.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Requirements and Assumptions](#requirements-and-assumptions)
- [Getting Started](#getting-started)
- [API Documentation — Swagger/OpenAPI](#api-documentation--swaggeropenapi)
- [Application Health — Actuator](#application-health--actuator)
- [REST API Reference](#rest-api-reference)
- [Testing](#testing)
- [Seed Data](#seed-data)
- [Performance Considerations](#performance-considerations)
- [Design Decisions and Trade-offs](#design-decisions-and-trade-offs)
- [AI-Assisted Development](#ai-assisted-development)
- [Future Improvements](#future-improvements)
- [Project Structure](#project-structure)
- [Assessment Submission Checklist](#assessment-submission-checklist)

## Overview

The application replaces spreadsheet-based salary administration with a web-based interface backed by a Spring Boot REST API and PostgreSQL database.

The HR Manager can:

- Find employees using search and filters.
- Browse employees through paginated results.
- View employee details.
- Update an employee's current salary.
- Review salary statistics by country and job title.
- Review employee counts by country and department.

**Currency principle:** Salary statistics are calculated within the relevant country and its currency. Salaries in different currencies are not added together or presented as a consolidated cross-country total.

## Features

### Employee Management

- Employee directory with search and filtering.
- Server-side pagination.
- Employee details lookup.
- Update current salary, currency, and effective date.
- Input validation for salary updates.

### HR Insights

- Employee counts by country and department.
- Minimum salary by country.
- Maximum salary by country.
- Average salary by country.
- Average salary by job title within a selected country.

These insights help HR understand salary ranges and differences between job titles without introducing a full payroll-processing system.

### Operational and Developer Features

- REST APIs built with Spring Boot.
- PostgreSQL persistence.
- OpenAPI documentation through Swagger UI.
- Spring Boot Actuator health monitoring.
- Backend and frontend automated tests.
- Fictional seed data targeting 10,000 employees.
- API testing through Postman.

## Technology Stack

| Layer | Technology |
|---|---|
| Backend | Java and Spring Boot |
| API | Spring Web / REST |
| Persistence | Spring Data JPA and Hibernate |
| Database | PostgreSQL |
| Frontend | Angular |
| Build tool | Maven |
| API documentation | OpenAPI / Swagger UI through springdoc-openapi |
| Health monitoring | Spring Boot Actuator |
| Backend testing | JUnit and Mockito, as configured |
| Frontend testing | Angular testing tools, as configured |
| API verification | Postman |
| Version control | Git and GitHub |

Check `pom.xml` and `frontend/package.json` for the exact dependency and framework versions used by the final build.

## Architecture

```text
                  +---------------------+
                  |      HR Manager     |
                  +----------+----------+
                             |
                             v
                  +---------------------+
                  |   Angular Frontend  |
                  | Dashboard / Employee|
                  | Directory / Insights|
                  +----------+----------+
                             |
                         HTTP / JSON
                             |
                             v
                  +---------------------+
                  | Spring Boot REST API|
                  | Controllers         |
                  +----------+----------+
                             |
                             v
                  +---------------------+
                  | Service Layer       |
                  | Business Logic      |
                  +----------+----------+
                             |
                             v
                  +---------------------+
                  | Spring Data JPA     |
                  | Repositories        |
                  +----------+----------+
                             |
                             v
                  +---------------------+
                  | PostgreSQL Database |
                  +---------------------+
```

### Backend Responsibilities

- **Controllers:** Handle HTTP requests, request parameters, validation integration, and HTTP responses.
- **Services:** Implement employee operations, salary updates, and salary-insight calculations.
- **Repositories:** Handle database access through Spring Data JPA.
- **Database:** Persist employee records and current salary information.

This separation keeps HTTP handling, business logic, and persistence responsibilities distinct.

## Requirements and Assumptions

### Primary User

The application is designed for a single HR Manager user.

### Assumptions

- The application uses fictional employee data for the assessment.
- The target dataset contains 10,000 employees across five countries, with 2,000 employees per country.
- Each employee has a current salary amount, currency code, and effective date.
- Salary summaries are calculated within each country's currency.
- The application uses a relational database.
- The assessment does not require login, roles, or an audit trail.

### In Scope

- Employee directory, search, filtering, and pagination.
- Employee details.
- Updating current salary information.
- Employee counts by country and department.
- Country-level minimum, maximum, and average salary.
- Average salary by job title within a country.
- Seed data for 10,000 employees.
- Backend and frontend tests.
- API documentation and health monitoring.

### Deliberately Out of Scope

- **Salary history:** Only the current salary is stored. Historical salary tracking could be introduced later using effective-dated salary records linked to each employee.
- **Authentication and authorisation:** Login, roles, and permission management are not implemented because the assessment assumes one HR Manager.
- **Audit tracking:** A separate record of who changed a salary and when is not required.
- **Currency conversion:** Cross-currency conversion and consolidated totals are not required.
- **Payroll processing:** Tax calculations, deductions, payslips, and bank transfers are excluded.
- **Employee self-service and notifications:** These are not needed for the assessment's primary workflow.

A future production version should consider authentication, access control, and audit logging because salary data is sensitive.

## Getting Started

### Prerequisites

- Java compatible with the backend's configured Spring Boot version. The project was developed using Java 21.
- Maven, or the Maven wrapper if included.
- PostgreSQL compatible with the database configuration.
- Node.js and npm compatible with the Angular project.

### 1. Clone the Repository

```bash
git clone https://github.com/soumyapokale/incubyte-salary-management.git
cd incubyte-salary-management
```

### 2. Configure PostgreSQL

Create a PostgreSQL database named `salary_management`, unless a different database name is specified in your local configuration.

Example SQL:

```sql
CREATE DATABASE salary_management;
```

Configure the datasource in `src/main/resources/application.properties` or `application.yml`.

Typical Spring Boot datasource properties look like this:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/salary_management
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Use the actual property names and database settings from the project configuration. Do not commit database passwords or other secrets to Git.

Ensure the database schema and seed process are configured as required by the application.

### 3. Start the Backend

From the repository root:

**Windows:**

```cmd
mvnw.cmd spring-boot:run
```

If the Maven wrapper is not present:

```cmd
mvn spring-boot:run
```

**macOS/Linux:**

```bash
./mvnw spring-boot:run
```

If the wrapper is not present:

```bash
mvn spring-boot:run
```

The backend uses port `8080` by default unless overridden by configuration.

### 4. Start the Frontend

Open a separate terminal:

```bash
cd frontend
npm install
npm start
```

Use the start script defined in `frontend/package.json` if its name differs.

The Angular development server is typically available at:

```text
http://localhost:4200
```

Ensure the frontend's API configuration points to the running backend.

### 5. Seed and Verify Employee Data

Run the seed script or database initialization process provided by the project.

Verify that the database contains the intended 10,000 fictional employees. Do not assume that data seeding runs automatically unless it is configured to do so.

## API Documentation — Swagger/OpenAPI

The backend uses springdoc-openapi to expose interactive documentation for the REST APIs.

When the backend is running locally:

| Resource | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| OpenAPI YAML | `http://localhost:8080/v3/api-docs.yaml` |

Swagger UI lets developers and reviewers:

- Browse the available endpoints.
- Inspect query parameters and request bodies.
- Review generated request and response schemas.
- Execute API requests from the browser.

### Dependency

The `springdoc-openapi-starter-webmvc-ui` dependency must be compatible with the Spring Boot version declared in `pom.xml`.

Example for a Spring Boot 4.x project:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.1.1</version>
</dependency>
```

For Spring Boot 3.x, use a compatible springdoc 2.x release instead. Do not add both dependency versions.

Refer to the official documentation for compatibility and configuration: https://springdoc.org/

The standard Swagger UI and OpenAPI paths are shown above. If the application uses a custom context path or security configuration, configure the routes accordingly.

## Application Health — Actuator

Spring Boot Actuator provides operational health information for the backend.

The local health endpoint is:

```text
http://localhost:8080/actuator/health
```

A successful response commonly looks like:

```json
{
  "status": "UP"
}
```

Depending on the health configuration, the response can also report the status of supporting components such as the database.

Expose only the Actuator endpoints required by the application. Detailed diagnostic information should not be made publicly accessible in a production environment.

## REST API Reference

Base URL:

```text
http://localhost:8080
```

### Employee APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/employees` | Search/filter employees and return paginated results |
| GET | `/api/employees/{id}` | Retrieve an employee by ID |
| PUT | `/api/employees/{id}/salary` | Update an employee's current salary |
| GET | `/api/employees/employee-counts` | Retrieve employee counts by country and department |

The employee-list endpoint supports pagination and the search/filter parameters implemented by the controller. Swagger UI provides the exact parameter definitions.

Pagination defaults to a page size of 10, with the requested size capped at 100.

### Salary Insights APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/insights/country-salaries` | Return minimum, maximum, and average salary statistics by country |
| GET | `/api/insights/job-title-salaries?country=India` | Return average salary grouped by job title for the selected country |

Salary insights are country-scoped and should preserve the associated currency.

### Example Salary Update Request

```http
PUT /api/employees/1/salary
Content-Type: application/json
```

Example request body:

```json
{
  "salary": 95000.00,
  "currency": "INR",
  "effectiveDate": "2026-10-01"
}
```

Use an existing employee ID and valid values when testing the request. Refer to Swagger UI for the actual request and response schemas.

### Validation

Salary updates should be checked for valid salary values, a three-letter currency code, and a valid effective date according to the backend's validation rules.

The API should return appropriate HTTP responses for validation errors and unknown employee IDs. Verify the precise response format against the implementation.

## Testing

The project includes backend and frontend automated tests.

### Run Backend Tests

Windows:

```cmd
mvnw.cmd test
```

macOS/Linux:

```bash
./mvnw test
```

If the Maven wrapper is not included, use:

```bash
mvn test
```

### Run Frontend Tests

```bash
cd frontend
npm test
```

Use the test script defined in `frontend/package.json` if it differs.

### Recommended Core Test Coverage

- Employee lookup and unknown employee IDs.
- Salary update validation and persistence.
- Country-level salary calculations.
- Job-title salary averages for a selected country.
- Search, filtering, and pagination bounds.
- Backend error handling.
- Frontend services and component behaviour.

Run the tests on the final code revision before submission. Any reported test count or coverage percentage should come from the actual final test run.

## Seed Data

The target dataset contains 10,000 fictional employees distributed across five countries, with 2,000 employees per country.

The seed process should be repeatable and should not rely on real employee information.

Before submission, verify:

1. The seed process can populate a clean database.
2. Exactly 10,000 intended employee records are created.
3. Search and pagination work across the seeded dataset.
4. Salary insights return country-scoped statistics.
5. Seed instructions match the implementation.

## Performance Considerations

### Server-Side Pagination

The API returns a limited page of employees rather than loading all 10,000 records in a single response. The default page size is 10, and the maximum page size is 100.

### Database-Side Filtering

Search, filtering, sorting, and salary aggregations should be handled by the backend and database rather than by downloading the complete employee dataset to the browser.

### Aggregate Queries

Country-level salary statistics and job-title averages should be calculated using backend/database operations. Calculating these statistics from only the current UI page would produce misleading results.

### Indexing

Indexes may be useful for frequently filtered fields such as country and department and for employee lookup. Index choices should be based on actual query patterns and database query plans.

### Measure Before Optimising

For 10,000 rows, straightforward relational queries are a reasonable starting point. Use representative measurements and query plans before introducing caching or additional infrastructure.

Offset pagination is sufficient as an initial approach for this assessment. Keyset pagination may be considered if larger datasets cause measurable performance problems.

## Design Decisions and Trade-offs

### 1. Current Salary Instead of Salary History

Only the current salary is stored because the assessment does not require historical salary reporting. This keeps the data model and update flow focused.

If salary history becomes a requirement, introduce effective-dated salary records and update the current salary and history transactionally.

### 2. No Authentication or Audit Trail

The assessment explicitly assumes a single HR Manager and excludes login, roles, and audit tracking.

For a real HR system, integrate the organisation's identity provider, enforce server-side authorisation, and record who changed salary data and when.

### 3. Country-Scoped Currency Insights

Salary statistics remain within each country's currency. Cross-country currency conversion would require an exchange-rate source, conversion date, and explicit reporting policy.

The application does not calculate consolidated totals across currencies.

### 4. Relational Database

PostgreSQL provides structured storage, relational integrity, and support for filtering and aggregate queries.

### 5. Layered Backend

Separating controllers, services, and repositories keeps HTTP concerns, business logic, and database access distinct, improving readability and testability.

### 6. API Rate Limiting

If the optional API rate limiter is enabled, its configured limit and implementation should be documented accurately. An in-memory per-IP limiter is generally limited to one application instance and is not a substitute for authentication or infrastructure-level protection.

## AI-Assisted Development

AI tools were used to assist with implementation, debugging, test drafting, API verification setup, and documentation.

Generated suggestions were treated as proposals and adapted to the project. API behaviour was manually exercised using Postman, and automated tests were added for core functionality.

For transparency, the supporting AI usage notes should record the actual tools and prompts used, along with examples of decisions or corrections made after reviewing generated output. Only claim validation steps that were actually performed.

## Future Improvements

The following features are intentionally outside the core assessment scope:

1. **Salary history:** Effective-dated salary records and a salary-change timeline.
2. **Authentication and authorisation:** Organisation identity integration and appropriate access controls.
3. **Audit logging:** Record the actor, timestamp, employee, previous value, and new value for salary changes.
4. **Operational readiness:** HTTPS, secret management, structured logs, metrics, health checks, backups, and deployment automation.
5. **Data governance:** Define allowed country/currency combinations and salary precision rules.
6. **Evidence-based optimisation:** Adjust indexes, caching, or pagination strategies based on measured query performance.
7. **Additional HR insights:** Median salary or salary distribution bands, when a clear HR question justifies them.

## Project Structure

The repository follows a layered Spring Boot backend and an Angular frontend.

```text
salary-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/incubyte/salary_management/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       └── ...
│   │   └── resources/
│   └── test/
├── frontend/
├── docs/
│   ├── requirements.md
│   ├── engineering-notes.md
│   └── ai-usage.md
├── pom.xml
└── README.md
```

