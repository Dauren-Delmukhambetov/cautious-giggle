create table if not exists addresses (
    id           bigint auto_increment primary key,

    address_line text       not null,
    city         text       not null,
    admin_area   text,
    postal_code  text       not null,
    country      text       not null,

    created_at   timestamp  not null,
    created_by   text       not null,
    updated_at   timestamp,
    updated_by   text,
    deleted_at   timestamp
);
