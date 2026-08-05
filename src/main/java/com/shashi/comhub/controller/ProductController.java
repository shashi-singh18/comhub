package com.shashi.comhub.controller;

import com.shashi.comhub.dto.ProductRequest;
import com.shashi.comhub.dto.ProductResponse;
import com.shashi.comhub.dto.error.ErrorResponse;
import com.shashi.comhub.entity.Product;
import com.shashi.comhub.mapper.ProductMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final ProductMapper productMapper;

    public ProductController(ProductService productService,
                             ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Operation(summary = "Create product")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        logger.info("Received request to create product. name={}", request.getName());

        Product product = productService.createProduct(request);

        ProductResponse response = productMapper.toResponse(product);

        logger.info("Returning response. Product created successfully. id={}", response.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        logger.info("Received request to fetch all products.");

        List<ProductResponse> response =
                productService.getAllProducts()
                        .stream()
                        .map(productMapper::toResponse)
                        .toList();

        logger.info("Returning {} products.", response.size());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long id) {

        logger.info("Received request to fetch product. id={}", id);

        ProductResponse response =
                productMapper.toResponse(
                        productService.getProductById(id));

        logger.info("Returning product. id={}", id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        logger.info("Received request to update product. id={}", id);

        ProductResponse response =
                productMapper.toResponse(
                        productService.updateProduct(id, request));

        logger.info("Returning updated product. id={}", id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        logger.info("Received request to delete product. id={}", id);

        productService.deleteProduct(id);

        logger.info("Product deleted successfully. id={}", id);

        return ResponseEntity.noContent().build();
    }
}