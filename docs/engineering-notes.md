# Engineering Notes: Architecture, Trade-offs, Performance, and Future Improvements

## 1. Architecture
The application is organised into three main parts:
- **Angular frontend:** dashboard, employee directory, salary editing, and salary-insight views.
- **Spring Boot REST API:** controllers expose employee and insight endpoints; services hold business logic; repositories access persisted data.
- **PostgreSQL:** relational storage for employee records and salary data.

The frontend communicates with the backend over HTTP. Employee listing uses server-side pagination rather than downloading all 10,000 rows. Salary insights are calculated by backend queries/services so the UI does not need to aggregate the entire employee list.

## 2. Key product and engineering decisions
- **Keep the workflow focused:** the main tasks are finding an employee, updating their current salary, and answering basic salary-distribution questions.
- **Store current salary only:** this meets the stated assessment scope and avoids introducing history semantics that the user did not request.
- **Keep salary insights country-scoped:** each country’s min/max/average and job-title averages use that country’s salary currency. No cross-currency total is presented.
- **Server-side pagination:** bounds response size and UI work. The API defaults to a page size of 10 and caps requested size at 100.
- **Layered backend:** controllers handle HTTP concerns, services own business operations, and repositories handle persistence. An `EmployeeService` interface separates the service contract from its implementation.
- **Relational database:** PostgreSQL fits structured employee records and aggregation queries and is suitable for the 10,000-row assessment dataset.
- **No authentication or audit trail:** explicitly excluded by the assessment. This is not a recommendation for a real production salary system.

## 3. Trade-offs and limitations
- **Current salary versus history:** a single current salary is simpler and meets the requirement, but it cannot answer questions about past salaries or explain previous changes. If needed later, add a salary-history table and update it transactionally whenever current salary changes.
- **No authentication:** fewer moving parts for the assessment, but anyone able to reach the API may be able to access or change salary data. Production deployment must add identity integration and server-side authorisation.
- **No audit trail:** simpler implementation, but changes cannot be attributed to a person or reconstructed. Add immutable audit events for production use.
- **Country currency assumption:** insights are meaningful within each country only. Cross-country comparisons require an explicit exchange-rate provider, conversion date/policy, and clear display of original and converted amounts.
- **Seeded fictional data:** makes the app reproducible and easy to demonstrate, but it does not represent real HR data or prove production data-governance readiness.
- **Optional rate limiting:** the API rate limiter can reduce bursts of requests, but an in-memory per-IP implementation is local to one application instance, may not share limits across instances, and needs careful proxy/IP handling. It should not be treated as a substitute for authentication or infrastructure-level protection.

## 4. Performance considerations
- **Pagination:** the API limits page size to 100 so clients cannot request the entire dataset in one response.
- **Database-side filtering and sorting:** search/filter/pagination should be performed by the database rather than loading all employees into application memory. Confirm query plans if the dataset or filters grow.
- **Indexes:** consider indexes on frequently filtered fields such as country and department, and on fields used for employee lookup. Add indexes based on measured query plans and actual query patterns rather than indexing every column.
- **Aggregations:** calculate minimum, maximum, and average salary, and job-title averages in the backend/database. Avoid computing these from only the currently visible page.
- **Bounded responses:** use DTOs/projections for aggregate endpoints where practical so the API returns only the fields the UI needs.
- **Measure before optimising:** with 10,000 rows, simple relational queries should generally be a reasonable starting point. Benchmark representative queries and inspect `EXPLAIN ANALYZE` before introducing caching or more complex infrastructure.
- **Known scaling limits:** very large offset-based pagination can become expensive at much larger row counts; keyset pagination may be considered if that becomes a measured bottleneck.

## 5. Testing strategy
- Unit-test salary update rules and validation, employee lookup behaviour, and salary-insight calculations.
- Test API success and error cases, including invalid salary/currency/date inputs and unknown employee IDs.
- Test pagination bounds and filtering behaviour.
- Test that a salary update persists and is returned by subsequent reads.
- Keep tests deterministic by using controlled test data and avoiding dependence on external services.
- Run backend and frontend test suites before submission and report actual results; do not claim coverage percentages unless generated by the final run.

## 6. AI-assisted development
AI tools were used to accelerate implementation, test drafting, documentation, and debugging. Generated suggestions were reviewed and adapted to the project rather than accepted blindly. API behaviour was manually exercised with Postman, and automated tests are used to check core logic. Before submission, record the specific tools/prompts used and any important human decisions or corrections in `docs/ai-usage.md`; do not invent prompts or claim reviews that did not occur.

## 7. Future improvements (not part of this assessment)
1. **Salary history:** add effective-dated salary records and a salary-change timeline.
2. **Authentication and authorisation:** integrate the organisation's identity provider and restrict salary updates to authorised HR users.
3. **Audit logging:** record actor, timestamp, employee, previous value, new value, and request/correlation ID.
4. **Operational readiness:** externalise secrets, configure HTTPS, centralised logs, health checks, metrics, backups, and deployment automation.
5. **Data validation and governance:** enforce allowed countries/currencies and define salary precision/rounding rules.
6. **Scale based on evidence:** add or adjust indexes after query-plan review; consider caching or keyset pagination only if metrics show a need.
7. **More insights if requested:** salary distribution bands, headcount by department/country, or median salary. These should be added only with a clear HR question and a defined calculation.

## 8. Pre-submission verification
- Confirm the seed script creates exactly 10,000 records on a clean database.
- Run backend and frontend tests and retain the real output/coverage report.
- Confirm salary updates persist after a reload.
- Verify that country salary summaries use the country's currency and do not combine currencies.
- Verify the latest commits are pushed and these documents are committed.
- Test the deployed frontend, API, and persistent database together.
- Record a short end-to-end demo and include its link in the submission email.
