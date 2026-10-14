# AI Usage Notes

AI tools were used as development assistants during implementation, debugging, test creation, and documentation. Their output was treated as a proposal, not as a source of truth.

## How AI was used
- Helped reason about application structure and REST API design.
- Assisted with drafting code and tests, troubleshooting build/runtime issues, and preparing Postman requests.
- Helped organise requirements, trade-offs, performance notes, and future improvements.

## Validation approach
- Exercise API endpoints with Postman.
- Run automated backend and frontend tests.
- Inspect generated code and adapt it to the existing project structure.
- Verify behaviour against the stated assessment requirements.

## Human decisions and limitations
- Kept the product focused on current salary management and the minimum required insights.
- Excluded salary history, authentication, and audit tracking because the assessment explicitly places them out of scope.
- Kept insights within each country's currency and avoided consolidated cross-currency totals.
- Any AI-generated code or documentation should be reviewed against the actual implementation before submission.

## Prompt record
Add a short list of the actual prompts that materially influenced the implementation, along with the relevant change or decision. Only record prompts that were actually used; do not reconstruct or invent a prompt history.
