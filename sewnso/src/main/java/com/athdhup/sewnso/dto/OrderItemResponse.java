package com.athdhup.sewnso.dto;

import com.athdhup.sewnso.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
    Long productVariantId,
    String sku,
    Integer quantity,
    BigDecimal priceAtPurchase
) {
    public static OrderItemResponse fromEntity(OrderItem item) {
        return new OrderItemResponse(
            item.getProductVariant().getId(),
            item.getProductVariant().getSku(),
            item.getQuantity(),
            item.getPriceAtPurchase()
        );
    }
}