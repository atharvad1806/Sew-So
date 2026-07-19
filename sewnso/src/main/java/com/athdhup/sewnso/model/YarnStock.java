package com.athdhup.sewnso.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;


/**
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
*/

@Entity
@Table(name = "yarn_stock")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class YarnStock{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "yarn_weight", nullable = false)
    private String yarnWeight;

    @Column(name = "fiber_type", nullable = false)
    private String fiberType;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "quantity_grams", nullable = false)
    private Integer quantityGrams;

    @Column(name = "reorder_threshold_grams", nullable = false)
    private Integer reorderThresholdGrams;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

}