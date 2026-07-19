CREATE TABLE yarn_stock (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    yarn_weight VARCHAR(100) NOT NULL,
    fiber_type VARCHAR(100) NOT NULL,
    color VARCHAR(100) NOT NULL,
    quantity_grams INT NOT NULL,
    reorder_threshold_grams INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
