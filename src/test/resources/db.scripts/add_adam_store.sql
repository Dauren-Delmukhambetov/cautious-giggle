
insert ignore into addresses (address_line, city, postal_code, country, created_at, created_by)
values('Pushkin street, 34', 'Minsk', '220069', 'BY', current_timestamp, 'adam.smith');

insert ignore into stores (name, mode, address_id, owner_id, created_at, created_by)
values ('Store #1', 'SELLER',
(select id from addresses where address_line = 'Pushkin street, 34' limit 1),
(select id from users where username = 'adam.smith'),
current_timestamp, 'adam.smith');
