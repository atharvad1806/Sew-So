package com.athdhup.sewnso.dto;

import com.athdhup.sewnso.model.Product;

public record ProductRequest(
    String name,
    Product.ProductCategory category,
    String description,
    String baseImageUrl
) {}