package com.shashi.comhub.mapper;

import com.shashi.comhub.dto.CategorySummaryResponse;
import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.dto.ProductResponse;
import com.shashi.comhub.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        product.setStock(request.getStock());

        // Category will be set in the Service layer.
        // Active is initialized in the entity.

        return product;
    }

    public ProductResponse toResponse(Product product) {

        CategorySummaryResponse categorySummary =
                new CategorySummaryResponse(
                        product.getCategory().getId(),
                        product.getCategory().getName()
                );

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getBrand(),
                product.getImageUrl(),
                product.getStock(),
                product.getActive(),
                categorySummary
        );
    }
}