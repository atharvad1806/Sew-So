package com.athdhup.sewnso.service;

import com.athdhup.sewnso.dto.OrderItemRequest;
import com.athdhup.sewnso.dto.OrderRequest;
import com.athdhup.sewnso.dto.OrderResponse;
import com.athdhup.sewnso.exception.InsufficientYarnStockException;
import com.athdhup.sewnso.exception.OrderConflictException;
import com.athdhup.sewnso.exception.ResourceNotFoundException;
import com.athdhup.sewnso.model.ProductVariant;
import com.athdhup.sewnso.model.ProductYarnRequirement;
import com.athdhup.sewnso.model.YarnStock;
import com.athdhup.sewnso.model.AppUser;
import com.athdhup.sewnso.model.Order;
import com.athdhup.sewnso.model.OrderItem;
import com.athdhup.sewnso.repository.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AppUserRepository appUserRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductYarnRequirementRepository yarnRequirementRepository;
    private final YarnStockRepository yarnStockRepository;

    public OrderService(
            OrderRepository orderRepository,
            AppUserRepository appUserRepository,
            ProductVariantRepository productVariantRepository,
            ProductYarnRequirementRepository yarnRequirementRepository,
            YarnStockRepository yarnStockRepository
    ) {
        this.orderRepository = orderRepository;
        this.appUserRepository = appUserRepository;
        this.productVariantRepository = productVariantRepository;
        this.yarnRequirementRepository = yarnRequirementRepository;
        this.yarnStockRepository = yarnStockRepository;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        AppUser user = appUserRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        try {
            for (OrderItemRequest itemRequest : request.items()) {
                ProductVariant variant = productVariantRepository.findById(itemRequest.productVariantId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Product variant not found with id: " + itemRequest.productVariantId()));

                // Step 1: check + deduct yarn for this variant
                deductYarnForVariant(variant, itemRequest.quantity());

                // Step 2: build the order item, snapshotting the price at time of purchase
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductVariant(variant);
                orderItem.setQuantity(itemRequest.quantity());
                orderItem.setPriceAtPurchase(variant.getPrice());
                orderItems.add(orderItem);

                total = total.add(variant.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            }
        } catch (ObjectOptimisticLockingFailureException ex) {
            // Another order modified the same yarn stock at the same time
            throw new OrderConflictException(
                    "Stock changed while processing your order. Please try again.");
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(savedOrder);
    }

    private void deductYarnForVariant(ProductVariant variant, Integer orderedQuantity) {
        List<ProductYarnRequirement> requirements =
                yarnRequirementRepository.findByProductVariantId(variant.getId());

        for (ProductYarnRequirement requirement : requirements) {
            YarnStock yarnStock = yarnStockRepository.findById(requirement.getYarnStock().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Yarn stock not found"));

            int neededGrams = requirement.getQuantityGrams() * orderedQuantity;

            if (yarnStock.getQuantityGrams() < neededGrams) {
                throw new InsufficientYarnStockException(
                        "Not enough '" + yarnStock.getColor() + " " + yarnStock.getFiberType() +
                        "' in stock for " + variant.getSku() +
                        ". Needed: " + neededGrams + "g, Available: " + yarnStock.getQuantityGrams() + "g");
            }

            yarnStock.setQuantityGrams(yarnStock.getQuantityGrams() - neededGrams);
            yarnStockRepository.save(yarnStock); // @Version field protects against concurrent deduction
        }
    }
}