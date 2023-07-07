CREATE TABLE IF NOT EXISTS company(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS employee(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    company_id INTEGER NOT NULL,
    CONSTRAINT fk__user__company_id FOREIGN KEY (company_id) REFERENCES company(id)
);