package com.shashi.comhub.service;

import com.shashi.comhub.dto.common.PageResponse;
import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.dto.ProductResponse;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    PageResponse<ProductResponse> getAllProducts(Pageable pageable);

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}
