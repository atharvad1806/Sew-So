package com.athdhup.sewnso.repository;

import com.athdhup.sewnso.model.YarnStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YarnStockRepository extends JpaRepository<YarnStock, Long> {

    // For an admin "low stock" alert view
    List<YarnStock> findByQuantityGramsLessThan(Integer threshold);

    List<YarnStock> findByColorAndFiberType(String color, String fiberType);
}