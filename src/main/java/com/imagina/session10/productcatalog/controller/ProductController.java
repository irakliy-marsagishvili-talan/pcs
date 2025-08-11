package com.imagina.session10.productcatalog.controller;

import com.imagina.session10.productcatalog.dto.ProductRequest;
import com.imagina.session10.productcatalog.dto.ProductResponse;
import com.imagina.session10.productcatalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        logger.info("Creating a new product");
        // Logic to create a product
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(productRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable long id) {
        logger.info("Fetching product with ID: {}", id);
        // Logic to get a product by ID
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        // Logic to get all products with pagination
        logger.info("Fetching all products with pagination: page={}, size={}, sortBy={}, sortDirection={}",
                page, size, sortBy, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageRequest = PageRequest.of(page, size, sort);

        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    /**
     * Update an existing product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        logger.info("Updating product with ID: {}", id);

        ProductResponse response = productService.updateExistingProduct(id, request);
        return ResponseEntity.ok(response);
    }
    /**
     * Delete a product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with ID: {}", id);

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search products by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String name) {
        logger.debug("Searching products with name: {}", name);

        List<ProductResponse> products = productService.searchProducts(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Find products in price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponse>> findProductsInPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {

        logger.debug("Finding products in price range: {} - {}", minPrice, maxPrice);

        List<ProductResponse> products = productService.findProductsInPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }




}
