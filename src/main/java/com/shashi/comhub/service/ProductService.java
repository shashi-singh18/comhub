package com.shashi.comhub.service;

import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductRequest request);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}
