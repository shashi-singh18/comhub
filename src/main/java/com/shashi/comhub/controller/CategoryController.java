package com.shashi.comhub.controller;

import com.shashi.comhub.dto.CategoryRequest;
import com.shashi.comhub.dto.CategoryResponse;
import com.shashi.comhub.dto.error.ErrorResponse;
import com.shashi.comhub.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
@Tag(
        name = "Category Management",
        description = "APIs for managing product categories."
)
public class CategoryController {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Create a new category",
            description = "Creates a new product category."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Category already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        logger.info("Received request to create category. name={}", request.getName());

        CategoryResponse response = categoryService.createCategory(request);

        logger.info("Returning response. Category created successfully. id={}", response.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        logger.info("Received request to fetch all categories.");

        List<CategoryResponse> response = categoryService.getAllCategories();

        logger.info("Returning {} categories.", response.size());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(
            @PathVariable Long id) {

        logger.info("Received request to fetch category. id={}", id);

        CategoryResponse response = categoryService.getCategoryById(id);

        logger.info("Returning category. id={}", id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        logger.info("Received request to update category. id={}", id);

        CategoryResponse response = categoryService.updateCategory(id, request);

        logger.info("Returning updated category. id={}, name={}",
                id,
                response.getName()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id) {

        logger.info("Received request to delete category. id={}", id);

        categoryService.deleteCategory(id);

        logger.info("Category deleted successfully. id={}", id);

        return ResponseEntity.noContent().build();
    }
}