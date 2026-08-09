package com.shashi.comhub.service;

import com.shashi.comhub.dto.common.PageResponse;
import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.dto.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    PageResponse<ProductResponse> getAllProducts(Pageable pageable);

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    PageResponse<ProductResponse> searchProducts(String name, String brand, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
