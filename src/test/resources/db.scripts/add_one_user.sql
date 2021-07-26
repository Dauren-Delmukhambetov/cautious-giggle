INSERT INTO users (id, username, first_name, last_name, password, created_at) OVERRIDING SYSTEM VALUE
VALUES (1, 'adam.smith', 'Adam', 'Smith', 'password', current_timestamp)
ON CONFLICT DO NOTHING;