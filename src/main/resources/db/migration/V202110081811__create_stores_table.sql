create table if not exists stores (
    id           bigint auto_increment primary key,

    name         text       not null,
    mode         text       not null,
    address_id   bigint     not null,
    owner_id     bigint     not null,

    created_at   timestamp  not null,
    created_by   text       not null,
    updated_at   timestamp,
    updated_by   text,
    deleted_at   timestamp
);