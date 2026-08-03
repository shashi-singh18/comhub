package com.shashi.comhub.controller;

import com.shashi.comhub.dto.CategoryRequest;
import com.shashi.comhub.dto.CategoryResponse;
import com.shashi.comhub.entity.Category;
import com.shashi.comhub.exception.CategoryAlreadyExistsException;
import com.shashi.comhub.exception.CategoryNotFoundException;
import com.shashi.comhub.mapper.CategoryMapper;
import com.shashi.comhub.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);

        Category savedCategory = categoryService.createCategory(category);

        CategoryResponse response = categoryMapper.toResponse(savedCategory);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categoryList = categoryService.getAllCategories();

        List<CategoryResponse> responseList = categoryList.stream().map(categoryMapper::toResponse).toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);

        CategoryResponse response = categoryMapper.toResponse(category);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);

        Category updatedCategory = categoryService.updateCategory(id, category);

        CategoryResponse response = categoryMapper.toResponse(updatedCategory);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
