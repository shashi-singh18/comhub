package com.shashi.comhub.controller;

import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.dto.ProductResponse;
import com.shashi.comhub.dto.CategorySummaryResponse;
import com.shashi.comhub.dto.common.PageResponse;
import com.shashi.comhub.exception.ProductNotFoundException;
import com.shashi.comhub.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    ProductRequest productRequest;
    ProductResponse productResponse;
    CategorySummaryResponse categorySummaryResponse;

    @BeforeEach
    void setUp() {

        productRequest = new ProductRequest();

        productRequest.setName("Cricket Bat");
        productRequest.setDescription("English willow bat for cricket");
        productRequest.setPrice(new BigDecimal("14599.00"));
        productRequest.setBrand("MRF");
        productRequest.setImageUrl(
                "https://images.comhub.com/products/cricket-bat-mrf.jpg"
        );
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
        productResponse.setImageUrl(
                "https://images.comhub.com/products/cricket-bat-mrf.jpg"
        );
        productResponse.setStock(10L);
        productResponse.setActive(true);
        productResponse.setCategory(categorySummaryResponse);
    }

    @Test
    void shouldCreateProduct() throws Exception {

        when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(productResponse);

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "name": "Cricket Bat",
                                      "description": "English willow bat for cricket",
                                      "price": 14599.00,
                                      "brand": "MRF",
                                      "imageUrl": "https://images.comhub.com/products/cricket-bat-mrf.jpg",
                                      "stock": 10,
                                      "categoryId": 4
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Cricket Bat"))
                .andExpect(jsonPath("$.description")
                        .value("English willow bat for cricket"))
                .andExpect(jsonPath("$.price").value(14599.00))
                .andExpect(jsonPath("$.brand").value("MRF"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.category.id").value(4))
                .andExpect(jsonPath("$.category.name").value("Sports"));

        verify(productService)
                .createProduct(any(ProductRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenProductNameIsBlank() throws Exception {

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "name": "",
                                      "description": "English willow bat for cricket",
                                      "price": 14599.00,
                                      "brand": "MRF",
                                      "imageUrl": "https://images.comhub.com/products/cricket-bat-mrf.jpg",
                                      "stock": 10,
                                      "categoryId": 4
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any(ProductRequest.class));
    }

    @Test
    void shouldReturnProducts() throws Exception {

        PageResponse<ProductResponse> pageResponse =
                createPageResponse(List.of(productResponse), 0, 10, 1);

        when(productService.getAllProducts(any(Pageable.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(
                        get("/api/v1/products")
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(10))
                .andExpect(jsonPath("$.data[0].name")
                        .value("Cricket Bat"))
                .andExpect(jsonPath("$.data[0].price")
                        .value(14599.00))
                .andExpect(jsonPath("$.data[0].brand")
                        .value("MRF"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.hasPrevious").value(false));

        verify(productService)
                .getAllProducts(any(Pageable.class));
    }

    @Test
    void shouldReturnEmptyProducts() throws Exception {

        PageResponse<ProductResponse> pageResponse =
                createPageResponse(List.of(), 0, 10, 0);

        pageResponse.setTotalPages(0);
        pageResponse.setFirst(true);
        pageResponse.setLast(true);
        pageResponse.setHasNext(false);
        pageResponse.setHasPrevious(false);

        when(productService.getAllProducts(any(Pageable.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(
                        get("/api/v1/products")
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.hasPrevious").value(false));

        verify(productService)
                .getAllProducts(any(Pageable.class));
    }

    @Test
    void shouldReturnProductWhenProductExists() throws Exception {

        when(productService.getProductById(10L))
                .thenReturn(productResponse);

        mockMvc.perform(
                        get("/api/v1/products/10")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name")
                        .value("Cricket Bat"))
                .andExpect(jsonPath("$.description")
                        .value("English willow bat for cricket"))
                .andExpect(jsonPath("$.price")
                        .value(14599.00))
                .andExpect(jsonPath("$.brand")
                        .value("MRF"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.category.id").value(4))
                .andExpect(jsonPath("$.category.name").value("Sports"));

        verify(productService)
                .getProductById(10L);
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {

        when(productService.getProductById(100L))
                .thenThrow(
                        new ProductNotFoundException(
                                "Product not found with id=100"
                        )
                );

        mockMvc.perform(
                        get("/api/v1/products/100")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Product not found with id=100"));

        verify(productService)
                .getProductById(100L);
    }

   @Test
    void shouldUpdateProduct() throws Exception {

        when(productService.updateProduct(
                eq(10L),
                any(ProductRequest.class)
        )).thenReturn(productResponse);

        mockMvc.perform(
                        put("/api/v1/products/10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "name": "Cricket Bat",
                                      "description": "English willow bat for cricket",
                                      "price": 14599.00,
                                      "brand": "MRF",
                                      "imageUrl": "https://images.comhub.com/products/cricket-bat-mrf.jpg",
                                      "stock": 10,
                                      "categoryId": 4
                                    }
                                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name")
                        .value("Cricket Bat"))
                .andExpect(jsonPath("$.price")
                        .value(14599.00))
                .andExpect(jsonPath("$.brand")
                        .value("MRF"));

        verify(productService)
                .updateProduct(eq(10L), any(ProductRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingProductWithBlankName()
            throws Exception {

        mockMvc.perform(
                        put("/api/v1/products/10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "name": "",
                                      "description": "English willow bat for cricket",
                                      "price": 14599.00,
                                      "brand": "MRF",
                                      "imageUrl": "https://images.comhub.com/products/cricket-bat-mrf.jpg",
                                      "stock": 10,
                                      "categoryId": 4
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .updateProduct(anyLong(), any(ProductRequest.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingProduct()
            throws Exception {

        when(productService.updateProduct(
                eq(100L),
                any(ProductRequest.class)
        )).thenThrow(
                new ProductNotFoundException(
                        "Product not found with id=100"
                )
        );

        mockMvc.perform(
                        put("/api/v1/products/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "name": "Cricket Bat",
                                      "description": "English willow bat for cricket",
                                      "price": 14599.00,
                                      "brand": "MRF",
                                      "imageUrl": "https://images.comhub.com/products/cricket-bat-mrf.jpg",
                                      "stock": 10,
                                      "categoryId": 4
                                    }
                                    """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Product not found with id=100"));

        verify(productService)
                .updateProduct(eq(100L), any(ProductRequest.class));
    }

    @Test
    void shouldDeleteProduct() throws Exception {

        doNothing()
                .when(productService)
                .deleteProduct(10L);

        mockMvc.perform(
                        delete("/api/v1/products/10")
                )
                .andExpect(status().isNoContent());

        verify(productService)
                .deleteProduct(10L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingProduct()
            throws Exception {

        doThrow(
                new ProductNotFoundException(
                        "Product not found with id=100"
                )
        )
                .when(productService)
                .deleteProduct(100L);

        mockMvc.perform(
                        delete("/api/v1/products/100")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Product not found with id=100"));

        verify(productService)
                .deleteProduct(100L);
    }

    @Test
    void shouldReturnProductsWithSearchParameters()
            throws Exception {

        PageResponse<ProductResponse> pageResponse =
                createPageResponse(List.of(productResponse), 0, 10, 1);

        when(productService.searchProducts(
                eq("Cricket Bat"),
                eq("MRF"),
                eq(new BigDecimal("10000")),
                eq(new BigDecimal("50000")),
                any(Pageable.class)
        )).thenReturn(pageResponse);

        mockMvc.perform(
                        get("/api/v1/products/search")
                                .param("name", "Cricket Bat")
                                .param("brand", "MRF")
                                .param("minPrice", "10000")
                                .param("maxPrice", "50000")
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(10))
                .andExpect(jsonPath("$.data[0].name")
                        .value("Cricket Bat"))
                .andExpect(jsonPath("$.data[0].brand")
                        .value("MRF"))
                .andExpect(jsonPath("$.data[0].price")
                        .value(14599.00))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(productService).searchProducts(
                eq("Cricket Bat"),
                eq("MRF"),
                eq(new BigDecimal("10000")),
                eq(new BigDecimal("50000")),
                any(Pageable.class)
        );
    }

    @Test
    void shouldReturnEmptyResultWhenNoProductsMatchSearch()
            throws Exception {

        PageResponse<ProductResponse> pageResponse =
                createPageResponse(List.of(), 0, 10, 0);

        pageResponse.setTotalPages(0);
        pageResponse.setFirst(true);
        pageResponse.setLast(true);
        pageResponse.setHasNext(false);
        pageResponse.setHasPrevious(false);

        when(productService.searchProducts(
                any(),
                any(),
                any(),
                any(),
                any(Pageable.class)
        )).thenReturn(pageResponse);

        mockMvc.perform(
                        get("/api/v1/products/search")
                                .param("name", "NonExistingProduct")
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.hasPrevious").value(false));

        verify(productService).searchProducts(
                eq("NonExistingProduct"),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        );
    }

    private PageResponse<ProductResponse> createPageResponse(
            List<ProductResponse> data,
            int page,
            int size,
            long totalElements
    ) {

        PageResponse<ProductResponse> response =
                new PageResponse<>();

        response.setData(data);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(totalElements);
        response.setTotalPages(
                totalElements == 0
                        ? 0
                        : (int) Math.ceil((double) totalElements / size)
        );
        response.setFirst(page == 0);
        response.setLast(
                totalElements == 0 ||
                        page == response.getTotalPages() - 1
        );
        response.setHasNext(false);
        response.setHasPrevious(page > 0);

        return response;
    }
}