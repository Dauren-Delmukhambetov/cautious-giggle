-- adding store items
-- expired one
insert ignore into store_items (product_id, store_id, price, amount, active_since, active_till, created_at, created_by)
values (1, (select id from stores where name = 'Store #1' limit 1), 123.98, 100, (current_timestamp - interval 12 month), (current_timestamp - interval 10 month), current_timestamp, 'adam.smith');

-- currently active one
insert ignore into store_items (product_id, store_id, price, amount, active_since, active_till, created_at, created_by)
values (1, (select id from stores where name = 'Store #1' limit 1), 567.98, 50, (current_timestamp - interval 1 month), (current_timestamp + interval 1 month), current_timestamp, 'adam.smith');

-- upcoming one
insert ignore into store_items (product_id, store_id, price, amount, active_since, active_till, created_at, created_by)
values (1, (select id from stores where name = 'Store #1' limit 1), 345.98, 10, (current_timestamp + interval 10 month), (current_timestamp + interval 12 month), current_timestamp, 'adam.smith');
