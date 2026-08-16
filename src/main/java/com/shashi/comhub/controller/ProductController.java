package com.shashi.comhub.controller;

import com.shashi.comhub.dto.common.PageResponse;
import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.dto.ProductResponse;
import com.shashi.comhub.dto.error.ErrorResponse;
import com.shashi.comhub.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/products")
@Tag(
        name = "Product Management",
        description = "APIs for managing products."
)
public class ProductController {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Create product",
            description = "Creates a new product."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ProductResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        logger.info(
                "Received request to create product. name={}",
                request.getName()
        );

        ProductResponse response =
                productService.createProduct(request);

        logger.info(
                "Returning response. Product created successfully. id={}",
                response.getId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get all products",
            description = "Fetches all products with pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully",
                    content = @Content(
                            schema = @Schema(
                                    implementation = PageResponse.class
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @ParameterObject Pageable pageable) {

        logger.info(
                "Received request to fetch products. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        PageResponse<ProductResponse> response =
                productService.getAllProducts(pageable);

        logger.info(
                "Returning {} products.",
                response.getData().size()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get product by id",
            description = "Fetches a product using its ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product retrieved successfully",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ProductResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long id) {

        logger.info(
                "Received request to fetch product. id={}",
                id
        );

        ProductResponse response =
                productService.getProductById(id);

        logger.info(
                "Returning product. id={}",
                id
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update product",
            description = "Updates an existing product."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ProductResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product or category not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        logger.info(
                "Received request to update product. id={}",
                id
        );

        ProductResponse response =
                productService.updateProduct(id, request);

        logger.info(
                "Returning updated product. id={}, name={}",
                id,
                response.getName()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete product",
            description = "Deletes an existing product."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Product deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        logger.info(
                "Received request to delete product. id={}",
                id
        );

        productService.deleteProduct(id);

        logger.info(
                "Product deleted successfully. id={}",
                id
        );

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search products",
            description = """
                    Searches products by name, brand, minimum price,
                    or maximum price with pagination and sorting.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Products fetched successfully",
                    content = @Content(
                            schema = @Schema(
                                    implementation = PageResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @ParameterObject Pageable pageable) {

        logger.info(
                "Received request to fetch products. " +
                        "name={}, brand={}, minPrice={}, maxPrice={}, " +
                        "page={}, size={}, sort={}",
                name,
                brand,
                minPrice,
                maxPrice,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        PageResponse<ProductResponse> response =
                productService.searchProducts(
                        name,
                        brand,
                        minPrice,
                        maxPrice,
                        pageable
                );

        logger.info(
                "Returning {} products with name={}, brand={}, " +
                        "minPrice={}, maxPrice={}",
                response.getData().size(),
                name,
                brand,
                minPrice,
                maxPrice
        );

        return ResponseEntity.ok(response);
    }
}