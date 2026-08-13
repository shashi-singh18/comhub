package com.shashi.comhub.service;

import com.shashi.comhub.dto.CategorySummaryResponse;
import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.dto.ProductResponse;
import com.shashi.comhub.dto.common.PageResponse;
import com.shashi.comhub.entity.Category;
import com.shashi.comhub.entity.Product;
import com.shashi.comhub.exception.CategoryNotFoundException;
import com.shashi.comhub.exception.ProductNotFoundException;
import com.shashi.comhub.mapper.PageMapper;
import com.shashi.comhub.mapper.ProductMapper;
import com.shashi.comhub.repository.CategoryRepository;
import com.shashi.comhub.repository.ProductRepository;
import com.shashi.comhub.service.impl.ProductServiceImpl;
import com.shashi.comhub.specification.ProductSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ProductMapper productMapper;

    @Mock
    PageMapper pageMapper;

    @InjectMocks
    ProductServiceImpl productService;

    Category category;
    Product product;
    ProductRequest productRequest;
    ProductResponse productResponse;
    CategorySummaryResponse categorySummaryResponse;
    PageResponse<ProductResponse> pageResponse;
    PageResponse<ProductResponse> emptyPageResponse;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(4L);
        category.setName("Sports");
        category.setDescription("Sports equipment products.");

        product = new Product();
        product.setId(10L);
        product.setName("Cricket Bat");
        product.setDescription("English willow bat for cricket");
        product.setPrice(new BigDecimal("14599.00"));
        product.setBrand("MRF");
        product.setImageUrl("https://images.comhub.com/products/cricket-bat-mrf.jpg");
        product.setStock(10L);
        product.setActive(true);
        product.setCategory(category);

        productRequest = new ProductRequest();
        productRequest.setName("Cricket Bat");
        productRequest.setDescription("English willow bat for cricket");
        productRequest.setPrice(new BigDecimal("14599.00"));
        productRequest.setBrand("MRF");
        productRequest.setImageUrl("https://images.comhub.com/products/cricket-bat-mrf.jpg");
        productRequest.setStock(10L);
        productRequest.setCategoryId(4L);

        categorySummaryResponse = new CategorySummaryResponse();
        categorySummaryResponse.setId(4L);
        categorySummaryResponse.setName("Sports");

        productResponse = new ProductResponse();
        productResponse.setId(10L);
        productResponse.setName("Cricket Bat");
        productResponse.setDescription("English willow bat for cricket");
        productResponse.setPrice(new BigDecimal("14599.00"));
        productResponse.setBrand("MRF");
        productResponse.setImageUrl("https://images.comhub.com/products/cricket-bat-mrf.jpg");
        productResponse.setStock(10L);
        productResponse.setActive(true);
        productResponse.setCategory(categorySummaryResponse);

        pageResponse = new PageResponse<>();
        pageResponse.setData(List.of(productResponse));
        pageResponse.setPage(0);
        pageResponse.setSize(10);
        pageResponse.setTotalElements(1);
        pageResponse.setTotalPages(1);
        pageResponse.setFirst(true);
        pageResponse.setLast(true);
        pageResponse.setHasNext(false);
        pageResponse.setHasPrevious(false);

        emptyPageResponse = new PageResponse<>();
        emptyPageResponse.setData(List.of());
        emptyPageResponse.setPage(0);
        emptyPageResponse.setSize(10);
        emptyPageResponse.setTotalElements(0);
        emptyPageResponse.setTotalPages(0);
        emptyPageResponse.setFirst(true);
        emptyPageResponse.setLast(true);
        emptyPageResponse.setHasNext(false);
        emptyPageResponse.setHasPrevious(false);
    }

    @Test
    void shouldCreateProductWhenCategoryExists() {
        when(categoryRepository.findById(productRequest.getCategoryId()))
                .thenReturn(Optional.of(category));

        when(productMapper.toEntity(productRequest))
                .thenReturn(product);

        when(productRepository.save(product))
                .thenReturn(product);

        when(productMapper.toResponse(product))
                .thenReturn(productResponse);

        ProductResponse response = productService.createProduct(productRequest);

        assertNotNull(response);
        assertEquals(productResponse, response);
        assertEquals(category, product.getCategory());

        verify(categoryRepository).findById(productRequest.getCategoryId());
        verify(productMapper).toEntity(productRequest);
        verify(productRepository).save(product);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldThrowExceptionWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(productRequest.getCategoryId()))
                .thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> productService.createProduct(productRequest)
        );

        assertEquals("Category not found with id=" + productRequest.getCategoryId(),
                exception.getMessage());

        verify(categoryRepository).findById(productRequest.getCategoryId());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldReturnProductsWhenExists() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
                );

        when(productRepository.findAll(pageable))
                .thenReturn(productPage);

        when(productMapper.toResponse(product))
                .thenReturn(productResponse);

        when(pageMapper.toPageResponse(any(Page.class)))
                .thenReturn(pageResponse);

        PageResponse<ProductResponse> response =
                productService.getAllProducts(pageable);

        assertNotNull(response);
        assertEquals(pageResponse, response);

        verify(productRepository).findAll(pageable);
        verify(productMapper).toResponse(product);
        verify(pageMapper).toPageResponse(any(Page.class));
    }

    @Test
    void shouldReturnEmptyPageWhenNoProductExists() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> emptyPage =
                new PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );

        when(productRepository.findAll(pageable))
                .thenReturn(emptyPage);

        when(pageMapper.toPageResponse(any(Page.class)))
                .thenReturn(emptyPageResponse);

        PageResponse<ProductResponse> response =
                productService.getAllProducts(pageable);

        assertNotNull(response);
        assertEquals(emptyPageResponse, response);

        verify(productRepository).findAll(pageable);
        verify(pageMapper).toPageResponse(any(Page.class));
    }

    @Test
    void shouldReturnProductWhenProductExists() {
        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        when(productMapper.toResponse(product))
                .thenReturn(productResponse);

        ProductResponse response = productService.getProductById(10L);

        assertNotNull(response);
        assertEquals(productResponse, response);

        verify(productRepository).findById(10L);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldThrowExceptionWhenGettingProductDoesNotExist() {
        when(productRepository.findById(100L))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(100L)
        );

        assertEquals("Product not found with id=" + 100L,
                exception.getMessage());

        verify(productRepository).findById(100L);
        verify(productMapper, never()).toResponse(any(Product.class));
    }

    @Test
    void shouldUpdateProductWhenProductAndCategoryExists() {
        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(productRequest.getCategoryId()))
                .thenReturn(Optional.of(category));

        when(productRepository.save(product))
                .thenReturn(product);

        when(productMapper.toResponse(product))
                .thenReturn(productResponse);

        ProductResponse response = productService.updateProduct(10L, productRequest);

        assertNotNull(response);
        assertEquals(productResponse, response);
        assertEquals(category, product.getCategory());

        verify(productRepository).findById(10L);
        verify(categoryRepository).findById(productRequest.getCategoryId());
        verify(productRepository).save(product);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingProductDoesNotExist() {
        when(productRepository.findById(100L))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(100L, productRequest)
        );

        assertEquals("Product not found with id=" + 100L,
                exception.getMessage());

        verify(productRepository).findById(100L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductExistsButCategoryDoesNotExists() {
        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(productRequest.getCategoryId()))
                .thenReturn(Optional.empty());


        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> productService.updateProduct(10L, productRequest)
        );

        assertEquals("Category not found with id=" + productRequest.getCategoryId(),
                exception.getMessage());

        verify(productRepository).findById(10L);
        verify(categoryRepository).findById(productRequest.getCategoryId());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldDeleteProductWhenProductExists() {
        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(10L);

        verify(productRepository).findById(10L);
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowExceptionDeletingProductDoesNotExist() {
        when(productRepository.findById(10L))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProduct(10L)
        );

        assertEquals("Product not found with id=" + 10L,
                exception.getMessage());

        verify(productRepository).findById(10L);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void shouldReturnProductsWhenProductExistsWithRequestedSearchParameters() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> page = new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
        );

        when(productRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(page);

        when(productMapper.toResponse(product))
                .thenReturn(productResponse);

        when(pageMapper.toPageResponse(any(Page.class)))
                .thenReturn(pageResponse);

        PageResponse<ProductResponse> response = productService.searchProducts(
                        "Cricket Bat",
                        "MRF",
                        new BigDecimal(10000),
                        new BigDecimal(50000),
                        pageable
        );

        assertNotNull(response);
        assertEquals(pageResponse, response);

        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(productMapper).toResponse(product);
        verify(pageMapper).toPageResponse(any(Page.class));
    }

    @Test
    void shouldReturnEmptyPageWhenNoProductExistsWithRequestedSearchParameters() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> emptyPage =
                new PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );

        when(productRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(emptyPage);

        when(pageMapper.toPageResponse(any(Page.class)))
                .thenReturn(emptyPageResponse);

        PageResponse<ProductResponse> response = productService.searchProducts(
                "Cricket Bat",
                "MRF",
                new BigDecimal(100),
                new BigDecimal(500),
                pageable
        );

        assertNotNull(response);
        assertEquals(emptyPageResponse, response);

        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(pageMapper).toPageResponse(any(Page.class));
    }
}
