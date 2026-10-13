
WITH generated AS (
    SELECT
        n,
        (ARRAY['Aarav', 'Priya', 'Rohan', 'Ananya', 'Vikram',
         'Neha', 'Arjun', 'Meera', 'Kabir', 'Ishita'])
   [((n - 1) % 10) + 1] AS first_name,
   (ARRAY['Sharma', 'Patel', 'Singh', 'Gupta', 'Reddy',
    'Khan', 'Joshi', 'Verma', 'Das', 'Mehta'])
   [((n - 1) % 10) + 1] AS last_name,
   (ARRAY['Engineering', 'Finance', 'HR', 'Sales', 'Operations'])
   [((n - 1) % 5) + 1] AS department,
   (ARRAY['Software Engineer', 'Analyst', 'Manager',
    'Consultant', 'Team Lead'])
   [((n - 1) % 5) + 1] AS job_title,
    CASE n % 5
    WHEN 0 THEN 'India'
    WHEN 1 THEN 'USA'
    WHEN 2 THEN 'UK'
    WHEN 3 THEN 'Germany'
    ELSE 'Canada'
END AS country
    FROM generate_series(1, 10000) AS series(n)
)
INSERT INTO employees (
    employee_code,
    first_name,
    last_name,
    email,
    department,
    country,
    job_title,
    salary,
    currency,
    effective_date
)
SELECT
    'EMP' || LPAD(n::text, 5, '0'),
    first_name,
    last_name,
    LOWER(first_name || '.' || last_name || n || '@example.com'),
    department,
    country,
    job_title,
    (45000 + ((n * 137) % 155000))::numeric(19, 2),
    CASE country
        WHEN 'India' THEN 'INR'
        WHEN 'USA' THEN 'USD'
        WHEN 'UK' THEN 'GBP'
        WHEN 'Germany' THEN 'EUR'
        ELSE 'CAD'
END,
    CURRENT_DATE - (n % 1000)
FROM generated
ON CONFLICT (employee_code) DO NOTHING;
