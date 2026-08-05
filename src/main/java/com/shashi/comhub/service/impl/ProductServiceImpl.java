package com.shashi.comhub.service.impl;

import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.entity.Category;
import com.shashi.comhub.entity.Product;
import com.shashi.comhub.exception.ProductNotFoundException;
import com.shashi.comhub.mapper.ProductMapper;
import com.shashi.comhub.repository.ProductRepository;
import com.shashi.comhub.service.CategoryService;
import com.shashi.comhub.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    private static final Logger logger =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryService categoryService,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productMapper = productMapper;
    }

    @Override
    public Product createProduct(ProductRequest request) {
        logger.info("Creating product with name={}",
                request.getName()
        );

        Category category = categoryService.getCategoryById(request.getCategoryId());

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        logger.info("Product created successfully with id={}, name={}",
                savedProduct.getId(),
                savedProduct.getName()
        );

        return savedProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");

        List<Product> products = productRepository.findAll();

        logger.info("Fetched {} products", products.size());

        return products;
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Product not found with id={}", id);

                    return new ProductNotFoundException("Product not found with id=" + id);
                });

        logger.info("Fetched product with id={}",
                id
        );

        return product;
    }

    @Override
    public Product updateProduct(Long id, ProductRequest request) {
        logger.info("Updating product with id={}, name={}",
                id,
                request.getName());

        Product existingProduct = getProductById(id);

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setStock(request.getStock());

        Category category = categoryService.getCategoryById(request.getCategoryId());

        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);

        logger.info("Product with id={}, name={} updated",
                id,
                request.getName()
        );

        return updatedProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        logger.info("Product with id={} is being deleted",
                id
        );

        Product product = getProductById(id);

        productRepository.delete(product);

        logger.info("Product deleted successfully. id={}",
                id
        );
    }
}
