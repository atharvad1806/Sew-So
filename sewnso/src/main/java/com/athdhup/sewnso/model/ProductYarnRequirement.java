package com.athdhup.sewnso.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
CREATE TABLE product_yarn_requirement (
    id BIGSERIAL PRIMARY KEY,
    product_variant_id BIGINT NOT NULL REFERENCES product_variant(id) ON DELETE CASCADE,
    yarn_stock_id BIGINT NOT NULL REFERENCES yarn_stock(id),
    quantity_grams INT NOT NULL
);
*/

@Entity
@Table(name = "product_yarn_requirement")
@Getter @Setter
public class ProductYarnRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yarn_stock_id", nullable = false)
    private YarnStock yarnStock;

    @Column(name = "quantity_grams", nullable = false)
    private Integer quantityGrams;
}