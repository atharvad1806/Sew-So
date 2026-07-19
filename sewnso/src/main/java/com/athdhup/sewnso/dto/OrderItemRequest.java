package com.athdhup.sewnso.dto;

public record OrderItemRequest(
    Long productVariantId,
    Integer quantity
) {}