CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) not null unique,
    password_hash varchar(255) not null,
    created_at timestampz not null default now()
);

create table expenses (
    id bigserial primary key,
    user_id bigint not null references users(id),
    amount numeric(12,2) not null check (amount >= 0),
    category varchar(20) not null,
    description text not null,
    expense_date date not null,
    created_at timestampz not null default now()
);

create index idx_expenses_user_date on expenses (user_id, expense_date desc);