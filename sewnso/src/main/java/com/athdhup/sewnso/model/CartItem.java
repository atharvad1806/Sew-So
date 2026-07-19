package com.athdhup.sewnso.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
CREATE TABLE cart_item (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL REFERENCES cart(id) ON DELETE CASCADE,
    product_variant_id BIGINT NOT NULL REFERENCES product_variant(id),
    quantity INT NOT NULL CHECK (quantity > 0)
);
*/

@Entity
@Table(name = "cart_item")
@Getter @Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;
}