CREATE TABLE IF NOT EXISTS users_roles(
    user_id   bigint  not null,
    role_name varchar not null,
    primary key (user_id, role_name)
);
