-- This script must be run in conjunction with the add_another_user.sql script to provide an owner ID

-- adding store address
insert ignore into addresses (address_line, city, postal_code, country, created_at, created_by, deleted_at)
values('5th avenue, 34', 'New York', '440076', 'US', current_timestamp, 'adam.smith', current_timestamp);
-- adding store
insert ignore into stores (id, name, mode, address_id, owner_id, created_at, created_by, deleted_at)
values (1, 'Deleted Store #9832', 'SELLER',
(select id from addresses where address_line = '5th avenue, 34' limit 1),
(select id from users where username = 'adam.smith'),
current_timestamp, 'adam.smith', current_timestamp);

-- adding store items
insert ignore into store_items (product_id, store_id, price, amount, active_since, active_till, created_at, created_by)
values (1, (select id from stores where name = 'Deleted Store #9832' limit 1), 123.98, 100, '2021-06-22 19:10:25', '2021-12-22 19:10:25', current_timestamp, 'adam.smith');
