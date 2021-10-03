alter table users
modify username varchar(256) not null;

alter table users
add constraint uq_users_username unique (username);
