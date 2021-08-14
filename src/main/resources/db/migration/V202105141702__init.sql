CREATE TABLE IF NOT EXISTS products(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    name text not null,
    image_link text,
    price numeric NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);
