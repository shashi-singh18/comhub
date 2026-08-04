package com.shashi.comhub.service.impl;

import com.shashi.comhub.entity.Category;
import com.shashi.comhub.exception.CategoryAlreadyExistsException;
import com.shashi.comhub.exception.CategoryNotFoundException;
import com.shashi.comhub.repository.CategoryRepository;
import com.shashi.comhub.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {
        logger.info("Creating category with name={}",
                category.getName()
        );

        if(categoryRepository.existsByName(category.getName())) {
            logger.warn("Category {} already exists",
                    category.getName()
            );

            throw new CategoryAlreadyExistsException(
                    "Category '" + category.getName() + "' already exists."
            );
        }

        Category savedCategory = categoryRepository.save(category);

        logger.info("Category created successfully with id={}, name={}",
                savedCategory.getId(),
                savedCategory.getName()
        );

        return savedCategory;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        logger.info("Fetched {} categories", categories.size());

        return categories;
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Category not found with id={}", id);

                    return new CategoryNotFoundException("Category not found with id=" + id);
                });

        logger.info("Fetched category with id={}",
                id
        );

        return category;
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        logger.info("Updating category with id={}, name={}",
                id,
                category.getName());

        if(categoryRepository.existsByName(category.getName())) {
            logger.warn("Category {} already exists",
                    category.getName()
            );

            throw new CategoryAlreadyExistsException(
                    "Category '" + category.getName() + "' already exists."
            );
        }

        Category existingCategory = getCategoryById(id);

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        Category updatedCategory = categoryRepository.save(existingCategory);;

        logger.info("Category with id={} updated",
                id
        );

        return updatedCategory;
    }

    @Override
    public void deleteCategory(Long id) {
        logger.info("Category with id={} is being deleted",
                id
        );

        Category existingCategory = getCategoryById(id);

        categoryRepository.delete(existingCategory);

        logger.info("Category deleted successfully. id={}",
                id
        );
    }
}
