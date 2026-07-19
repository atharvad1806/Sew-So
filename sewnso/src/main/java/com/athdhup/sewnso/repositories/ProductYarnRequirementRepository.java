package com.athdhup.sewnso.repository;

import com.athdhup.sewnso.model.ProductYarnRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductYarnRequirementRepository extends JpaRepository<ProductYarnRequirement, Long> {

    // The key lookup for order placement: "what yarn does this variant need?"
    List<ProductYarnRequirement> findByProductVariantId(Long productVariantId);
}