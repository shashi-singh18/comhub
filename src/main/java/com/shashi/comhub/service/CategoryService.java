package com.shashi.comhub.service;

import com.shashi.comhub.dto.CategoryRequest;
import com.shashi.comhub.dto.CategoryResponse;
import com.shashi.comhub.dto.common.PageResponse;

import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    PageResponse<CategoryResponse> getAllCategories(Pageable pageable);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}
