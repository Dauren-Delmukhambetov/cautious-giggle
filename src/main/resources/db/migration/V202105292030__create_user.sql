CREATE TABLE IF NOT EXISTS users(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    username text not null,
    first_name text not null,
    last_name text not null,
    email text,
    phone text,
    password text not null,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);
