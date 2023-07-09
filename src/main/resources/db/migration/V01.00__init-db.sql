CREATE TABLE IF NOT EXISTS public.company(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    number_of_employees INTEGER
);

CREATE TABLE IF NOT EXISTS public.employee(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    company_id INTEGER NOT NULL,
    CONSTRAINT fk__user__company_id FOREIGN KEY (company_id) REFERENCES company(id)
);