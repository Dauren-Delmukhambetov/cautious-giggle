create table if not exists store_items (
    item_id         bigint auto_increment primary key,

    product_id      bigint        not null,
    store_id        bigint        not null,
    price           numeric(9, 2) not null,
    amount          numeric(9, 2) not null,
    measure_unit    varchar(256)  not null default 'pcs',
    active_since    timestamp     not null default current_timestamp,
    active_till     timestamp     not null,

    created_at   timestamp  not null,
    created_by   text       not null,
    updated_at   timestamp,
    updated_by   text,
    deleted_at   timestamp
);