CREATE TABLE users(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username text not null,
    first_name varchar not null,
    last_name varchar not null,
    email varchar,
    phone varchar,
    password varchar not null,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);
