-- adding store items
-- expired one
insert ignore into store_items (product_id, store_id, price, amount, active_since, active_till, created_at, created_by)
values (1, (select id from stores where name = 'Store #1' limit 1), 123.98, 100, '2015-06-01 00:10:25', '2015-08-31 23:59:25', current_timestamp, 'adam.smith');

-- upcoming one
insert ignore into store_items (product_id, store_id, price, amount, active_since, active_till, created_at, created_by)
values (1, (select id from stores where name = 'Store #1' limit 1), 123.98, 100, '2025-06-01 00:10:25', '2025-08-31 23:59:25', current_timestamp, 'adam.smith');
