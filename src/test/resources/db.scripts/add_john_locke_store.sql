-- This script must be run in conjunction with the add_another_user.sql script to provide an owner ID
insert ignore into addresses (address_line, city, postal_code, country, created_at, created_by)
values('Shakespeare street, 12/34', 'Minsk', '220059', 'BY', current_timestamp, 'john.locke');

insert ignore into stores (name, mode, address_id, owner_id, created_at, created_by)
values ('Store #23/67', 'SELLER',
(select id from addresses where address_line = 'Shakespeare street, 12/34' limit 1),
(select id from users where username = 'john.locke'),
current_timestamp, 'john.locke');
