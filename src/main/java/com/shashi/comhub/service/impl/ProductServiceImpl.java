package com.shashi.comhub.service.impl;

import com.shashi.comhub.dto.common.PageResponse;
import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.dto.ProductResponse;
import com.shashi.comhub.entity.Category;
import com.shashi.comhub.entity.Product;
import com.shashi.comhub.exception.CategoryNotFoundException;
import com.shashi.comhub.exception.ProductNotFoundException;
import com.shashi.comhub.mapper.PageMapper;
import com.shashi.comhub.mapper.ProductMapper;
import com.shashi.comhub.repository.CategoryRepository;
import com.shashi.comhub.repository.ProductRepository;
import com.shashi.comhub.service.ProductService;
import com.shashi.comhub.specification.ProductSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final PageMapper pageMapper;

    private static final Logger logger =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ProductMapper productMapper,
                              PageMapper pageMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.pageMapper = pageMapper;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        logger.info("Creating product with name={}",
                request.getName()
        );

        Category category = getCategoryEntityById(request.getCategoryId());

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        logger.info("Product created successfully with id={}, name={}",
                savedProduct.getId(),
                savedProduct.getName()
        );

        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(Pageable pageable) {
        logger.info(
                "Fetching products. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Product> products = productRepository.findAll(pageable);

        logger.info("Fetched page {} containing {} products out of {} total products.",
                products.getNumber(),
                products.getNumberOfElements(),
                products.getTotalElements());

        Page<ProductResponse> productResponse = products.map(productMapper::toResponse);

        return pageMapper.toPageResponse(productResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = getProductEntityById(id);

        logger.info("Fetched product with id={}",
                id
        );

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        logger.info("Updating product with id={}, name={}",
                id,
                request.getName());

        Product existingProduct = getProductEntityById(id);

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setStock(request.getStock());

        Category category = getCategoryEntityById(request.getCategoryId());

        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);

        logger.info("Product with id={}, name={} updated",
                id,
                request.getName()
        );

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Product with id={} is being deleted",
                id
        );

        Product product = getProductEntityById(id);

        productRepository.delete(product);

        logger.info("Product deleted successfully. id={}",
                id
        );
    }

    @Override
    public PageResponse<ProductResponse> searchProducts(String name,
                                                        String brand,
                                                        BigDecimal minPrice,
                                                        BigDecimal maxPrice,
                                                        Pageable pageable) {
        logger.info(
                "Fetching products with name={}, brand={}, minPrice{}, maxPrice{}, page={}, size={}, sort={}",
                name,
                brand,
                minPrice,
                maxPrice,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Specification<Product> specification = ProductSpecification.build(
                name,
                brand,
                minPrice,
                maxPrice
        );

        Page<Product> products = productRepository.findAll(specification, pageable);

        logger.info(
                "Fetched page {} containing {} products with name={}, brand={}, minPrice{}, maxPrice{} out of {} total products.",
                products.getNumber(),
                products.getNumberOfElements(),
                name,
                brand,
                minPrice,
                maxPrice,
                products.getTotalElements()
        );

        Page<ProductResponse> productResponse = products.map(productMapper::toResponse);

        return pageMapper.toPageResponse(productResponse);
    }

    private Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Product not found with id={}", id);

                    return new ProductNotFoundException(
                            "Product not found with id=" + id
                    );
                });
    }

    private Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Category not found with id={}", id);

                    return new CategoryNotFoundException(
                            "Category not found with id=" + id
                    );
                });
    }
}
