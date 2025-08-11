package com.imagina.session10.productcatalog.dto;

import com.imagina.session10.productcatalog.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
    long id,
    String name,
    String description,
    BigDecimal price,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ProductResponse from (Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}
