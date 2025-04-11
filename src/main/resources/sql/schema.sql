CREATE SCHEMA IF NOT EXISTS ala_discounts;

CREATE TABLE IF NOT EXISTS products
(
    id          UUID NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
    );

DO $$
    BEGIN IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'discount_policy') THEN
    CREATE TYPE discount_policy AS ENUM ('QUANTITY', 'FIXED');
    END IF;
END$$;

CREATE TABLE IF NOT EXISTS discounts
(
    id          UUID NOT NULL,
    product_id  UUID NOT NULL,
    policy      discount_policy NOT NULL,
    CONSTRAINT pk_discounts PRIMARY KEY (id),
    CONSTRAINT fk_discounts_product FOREIGN KEY (product_id)
    REFERENCES products (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS discounts_thresholds
(
    id              UUID NOT NULL,
    discount_id     UUID NOT NULL,
    threshold_min   INTEGER,
    threshold_max   INTEGER,
    discount_value  DECIMAL(5, 2),
    CONSTRAINT pk_discounts_thresholds PRIMARY KEY (id),
    CONSTRAINT fk_discounts_thresholds_discount FOREIGN KEY (discount_id)
    REFERENCES discounts (id) ON DELETE CASCADE
    );
