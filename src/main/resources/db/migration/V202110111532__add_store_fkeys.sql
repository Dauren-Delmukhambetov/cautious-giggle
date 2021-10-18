alter table stores
    add constraint fk_stores_address foreign key (address_id)
    references addresses (id)
    on delete cascade
    on update cascade;

alter table stores
    add constraint fk_stores_owner foreign key (owner_id)
    references users (id)
    on delete no action
    on update no action;