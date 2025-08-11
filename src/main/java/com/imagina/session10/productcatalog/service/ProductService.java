package com.imagina.session10.productcatalog.service;

import com.imagina.session10.productcatalog.dto.ProductRequest;
import com.imagina.session10.productcatalog.dto.ProductResponse;
import com.imagina.session10.productcatalog.entity.Product;
import com.imagina.session10.productcatalog.exception.ProductAlreadyExistsException;
import com.imagina.session10.productcatalog.exception.ProductNotFoundException;
import com.imagina.session10.productcatalog.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest){
        logger.info("Creating product with name: {}", productRequest.name());

        if(productRepository.existsByNameIgnoreCase(productRequest.name())){
            logger.error("Product with name {} already exists", productRequest.name());
            throw new ProductAlreadyExistsException(productRequest.name());
        }

        var product = productRequest.toEntity();
        var savedProduct = productRepository.save(product);
        logger.info("Product created with ID: {}", savedProduct.getId());
        return ProductResponse.from(savedProduct);
    }

    public ProductResponse getProductById(long id) {
        logger.info("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        logger.info("Fetching all products with pagination: {}", pageable);
        return productRepository.findAll(pageable)
                .map(ProductResponse::from);

    }

    public ProductResponse updateExistingProduct(long id, ProductRequest productRequest) {
        logger.info("Updating product with ID: {}", id);

        var existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));


        if(productRepository.existsByNameIgnoreCase(productRequest.name()) &&
           !productRepository.findById(id).get().getName().equalsIgnoreCase(productRequest.name())) {
            logger.error("Product with name {} already exists", productRequest.name());
            throw new ProductAlreadyExistsException(productRequest.name());
        }

        existingProduct.setName(productRequest.name());
        existingProduct.setDescription(productRequest.description());
        existingProduct.setPrice(productRequest.price());

        var savedProduct = productRepository.save(existingProduct);
        logger.info("Successfully updated product with ID: {}", savedProduct.getId());
        return ProductResponse.from(savedProduct);

    }


    public void deleteProduct(long id) {
        logger.info("Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            logger.error("Product with ID {} not found", id);
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        logger.info("Successfully deleted product with ID: {}", id);
    }

    public List<ProductResponse> searchProducts(String name) {
        logger.debug("Searching products with name containing: {}", name);

        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }

    public List<ProductResponse> findProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Finding products in price range: {} - {}", minPrice, maxPrice);

        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }



}
