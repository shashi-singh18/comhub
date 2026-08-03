package com.shashi.comhub.service.impl;

import com.shashi.comhub.entity.Category;
import com.shashi.comhub.exception.CategoryAlreadyExistsException;
import com.shashi.comhub.exception.CategoryNotFoundException;
import com.shashi.comhub.repository.CategoryRepository;
import com.shashi.comhub.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {
        if(categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistsException("Category '" + category.getName() + "' already exists.");
        }
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        if(categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistsException("Category '" + category.getName() + "' already exists.");
        }

        Category existingCategory = getCategoryById(id);

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category existingCategory = getCategoryById(id);
        categoryRepository.delete(existingCategory);
    }
}
