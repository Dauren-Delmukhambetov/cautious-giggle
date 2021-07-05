INSERT INTO products (id, name, price, created_at) OVERRIDING SYSTEM VALUE
VALUES (1, 'Product # 1', 123.45, current_timestamp);
