CREATE TABLE expenses(
    id BIGSERIAL PRIMARY KEY,
    amount DECIMAL(10, 2),
    category VARCHAR(255),
    description TEXT,
    expense_date DATE,
    user_id BIGINT
);