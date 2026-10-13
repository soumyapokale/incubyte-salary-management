
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

    (ARRAY[
         'Aarav', 'Priya', 'Rohan', 'Ananya', 'Vikram',
     'Neha', 'Arjun', 'Meera', 'Kabir', 'Ishita'
         ])[((n - 1) % 10) + 1],

    (ARRAY[
        'Sharma', 'Patel', 'Singh', 'Gupta', 'Reddy',
        'Khan', 'Joshi', 'Verma', 'Das', 'Mehta'
    ])[((n - 1) % 10) + 1],

    LOWER(
        (ARRAY[
            'Aarav', 'Priya', 'Rohan', 'Ananya', 'Vikram',
            'Neha', 'Arjun', 'Meera', 'Kabir', 'Ishita'
        ])[((n - 1) % 10) + 1]
        || '.' ||
        (ARRAY[
            'Sharma', 'Patel', 'Singh', 'Gupta', 'Reddy',
            'Khan', 'Joshi', 'Verma', 'Das', 'Mehta'
        ])[((n - 1) % 10) + 1]
        || n || '@example.com'
    ),

    (ARRAY[
        'Engineering', 'Finance', 'HR', 'Sales', 'Operations'
    ])[((n - 1) % 5) + 1],

    CASE n % 5
        WHEN 0 THEN 'India'
        WHEN 1 THEN 'USA'
        WHEN 2 THEN 'UK'
        WHEN 3 THEN 'Germany'
        ELSE 'Canada'
END,

    (ARRAY[
        'Software Engineer', 'Analyst', 'Manager',
        'Consultant', 'Team Lead'
    ])[((n - 1) % 5) + 1],

    (45000 + ((n * 137) % 155000))::numeric(19, 2),

    CASE n % 5
        WHEN 0 THEN 'INR'
        WHEN 1 THEN 'USD'
        WHEN 2 THEN 'GBP'
        WHEN 3 THEN 'EUR'
        ELSE 'CAD'
END,

    CURRENT_DATE - (n % 1000)

FROM generate_series(1, 10000) AS series(n)

ON CONFLICT (employee_code) DO NOTHING;
