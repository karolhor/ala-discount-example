--- products
INSERT INTO products (id, name, description, price)
VALUES ('1c8a9b2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', 'Apple MacBook Air M2',
        'Lightweight and powerful laptop with Apple M2 chip, 13-inch Retina display, and long battery life.', 4799.00);

INSERT INTO products (id, name, description, price)
VALUES ('f9e8d7c6-b5a4-3210-fedc-ba9876543210', 'Sony 65-inch 4K UHD Smart TV',
        'High-resolution smart television with vibrant colors and smart features.', 6299.99);

INSERT INTO products (id, name, description, price)
VALUES ('a0b1c2d3-e4f5-6789-0abc-def012345678', 'Bose QuietComfort 45 Headphones',
        'Comfortable over-ear headphones with excellent noise cancellation and clear audio.', 1299.00);

INSERT INTO products (id, name, description, price)
VALUES ('98765432-fedc-ba98-7654-3210abcdef01', 'Logitech MX Master 3S Mouse',
        'Ergonomic wireless mouse with precise tracking, customizable buttons, and fast scrolling.', 449.99);

---- discounts
-- Logitech MX Master 3S Mouse, only fixed
INSERT INTO discounts (id, product_id, policy)
VALUES ('5e9f8a7b-c1d2-3e4f-5a6b-7c8d9e0f1a2b', '98765432-fedc-ba98-7654-3210abcdef01', 'FIXED');

-- Bose QuietComfort 45 Headphones, only quantity
INSERT INTO discounts (id, product_id, policy)
VALUES ('b3c4d5e6-f7a8-9b0c-1d2e-3f4a5b6c7d8e', 'a0b1c2d3-e4f5-6789-0abc-def012345678', 'QUANTITY');

-- Apple MacBook Air M2, mixed
INSERT INTO discounts (id, product_id, policy)
VALUES ('e2449f96-a7c3-44e7-b7a0-8fad7f65638a', '1c8a9b2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', 'FIXED');

INSERT INTO discounts (id, product_id, policy)
VALUES ('0d420264-3c50-496a-8334-bc8423679312', '1c8a9b2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d', 'QUANTITY');

---- discount thresholds
-- Logitech MX Master 3S Mouse, only fixed
INSERT INTO discounts_thresholds (id, discount_id, threshold_min, threshold_max, discount_value)
VALUES ('2b3a4c5d-6e7f-8a9b-0c1d-2e3f4a5b6c7d', '5e9f8a7b-c1d2-3e4f-5a6b-7c8d9e0f1a2b', NULL, NULL, 7.00);

-- Bose QuietComfort 45 Headphones, only quantity
INSERT INTO discounts_thresholds (id, discount_id, threshold_min, threshold_max, discount_value)
VALUES ('a67dfefc-794c-41cc-91c0-3a1e030b7ebc', 'b3c4d5e6-f7a8-9b0c-1d2e-3f4a5b6c7d8e', 1, 9, 0.00);

INSERT INTO discounts_thresholds (id, discount_id, threshold_min, threshold_max, discount_value)
VALUES ('970c74e0-35d8-41a0-bb5c-9585dfd7dff0', 'b3c4d5e6-f7a8-9b0c-1d2e-3f4a5b6c7d8e', 10, 19, 5.00);

INSERT INTO discounts_thresholds (id, discount_id, threshold_min, threshold_max, discount_value)
VALUES ('e419d4fa-01de-43b6-bf9f-48869c0bb063', 'b3c4d5e6-f7a8-9b0c-1d2e-3f4a5b6c7d8e', 20, 49, 10.00);

INSERT INTO discounts_thresholds (id, discount_id, threshold_min, threshold_max, discount_value)
VALUES ('0c370f00-1998-4506-8060-763f6892b099', 'b3c4d5e6-f7a8-9b0c-1d2e-3f4a5b6c7d8e', 50, NULL, 15.00);

-- Apple MacBook Air M2, mixed
INSERT INTO discounts_thresholds (id, discount_id, threshold_min, threshold_max, discount_value)
VALUES ('72008904-c27e-4c13-839e-33657e397ecf', 'e2449f96-a7c3-44e7-b7a0-8fad7f65638a', NULL, NULL, 1.50);

INSERT INTO discounts_thresholds (id, discount_id, threshold_min, threshold_max, discount_value)
VALUES ('1cca99a0-937e-4b96-a0d1-a8373009b76f', '0d420264-3c50-496a-8334-bc8423679312', 15, 39, 5.00);

INSERT INTO discounts_thresholds (id, discount_id, threshold_min, threshold_max, discount_value)
VALUES ('9925d01c-c0cd-46af-ad49-20ea8a911c47', '0d420264-3c50-496a-8334-bc8423679312', 40, NULL, 8.00);
