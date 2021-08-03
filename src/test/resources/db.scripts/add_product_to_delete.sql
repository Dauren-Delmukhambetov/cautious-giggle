INSERT INTO products (id, name, price, created_at) OVERRIDING SYSTEM VALUE
VALUES (2, 'Product # 2 (for deletion)', 98.765, current_timestamp)
ON CONFLICT DO NOTHING;