-- One test user
INSERT INTO app_user (email, password_hash, role, created_at)
VALUES ('test@sewnso.com', 'placeholder-hash', 'CUSTOMER', now());

-- One yarn stock entry
INSERT INTO yarn_stock (brand, yarn_weight, fiber_type, color, quantity_grams, reorder_threshold_grams, created_at, version)
VALUES ('Lion Brand', 'Worsted', 'Acrylic', 'Sage Green', 1000, 100, now(), 0);

-- One product
INSERT INTO product (name, category, description, base_image_url, created_at)
VALUES ('Chunky Beanie', 'KNIT', 'A cozy, made-to-order chunky knit beanie.', NULL, now());

-- One variant of that product
INSERT INTO product_variant (product_id, size, color, sku, price, created_at)
VALUES (
    (SELECT id FROM product WHERE name = 'Chunky Beanie'),
    'One Size',
    'Sage Green',
    'BEANIE-SAGE-OS',
    28.00,
    now()
);

-- Link the variant to the yarn it requires: 150g per beanie
INSERT INTO product_yarn_requirement (product_variant_id, yarn_stock_id, quantity_grams)
VALUES (
    (SELECT id FROM product_variant WHERE sku = 'BEANIE-SAGE-OS'),
    (SELECT id FROM yarn_stock WHERE color = 'Sage Green' AND brand = 'Lion Brand'),
    150
);