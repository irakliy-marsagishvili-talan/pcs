package com.imagina.session10.productcatalog.exception;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String productName) {
        super("Product already exists with name: " + productName);
    }
}