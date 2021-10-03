alter table products
add column created_by text not null
after created_at;

alter table products
modify column created_by text not null
comment 'refers to username in users table';

alter table products
add column updated_by text
after updated_at;

alter table products
modify column updated_by text
comment 'refers to username in users table';
