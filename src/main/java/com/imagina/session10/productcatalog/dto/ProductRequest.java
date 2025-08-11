package com.imagina.session10.productcatalog.dto;


import com.imagina.session10.productcatalog.entity.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
    @NotBlank(message = "Product name is required") @Size(max = 50) String name,
    @Size(max = 100) String description,
    @DecimalMin(value="0.01") BigDecimal price
) {
    public Product toEntity() {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        return product;
    }
}
