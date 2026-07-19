package com.athdhup.sewnso.dto;

import com.athdhup.sewnso.model.Product;

import java.time.LocalDateTime;

public record ProductResponse(
    Long id,
    String name,
    Product.ProductCategory category,
    String description,
    String baseImageUrl,
    LocalDateTime createdAt
) {
    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getCategory(),
            product.getDescription(),
            product.getBaseImageUrl(),
            product.getCreatedAt()
        );
    }
}