package com.imagina.session10.productcatalog.repository;

import com.imagina.session10.productcatalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByNameContainingIgnoreCaseAndPriceLessThanEqual(String name, BigDecimal maxPrice);

    @Query("SELECT p FROM Product p WHERE p.price > :price ORDER BY p.price ASC")
    List<Product> findProductsAbovePrice(@Param("price") BigDecimal price);


    @Query("SELECT p FROM Product p WHERE p.name ILIKE %:keyword% OR p.description ILIKE %:keyword%")
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Check if product exists by name (case-insensitive)
    boolean existsByNameIgnoreCase(String name);
}
