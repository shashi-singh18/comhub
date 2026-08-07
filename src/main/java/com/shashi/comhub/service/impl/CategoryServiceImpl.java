package com.shashi.comhub.service.impl;

import com.shashi.comhub.dto.CategoryRequest;
import com.shashi.comhub.dto.CategoryResponse;
import com.shashi.comhub.entity.Category;
import com.shashi.comhub.exception.CategoryAlreadyExistsException;
import com.shashi.comhub.exception.CategoryNotFoundException;
import com.shashi.comhub.mapper.CategoryMapper;
import com.shashi.comhub.repository.CategoryRepository;
import com.shashi.comhub.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private static final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        logger.info("Creating category with name={}",
                request.getName()
        );

        if (categoryRepository.existsByName(request.getName())) {

            logger.warn("Category {} already exists", request.getName());

            throw new CategoryAlreadyExistsException(
                    "Category '" + request.getName() + "' already exists."
            );
        }

        Category category = categoryMapper.toEntity(request);

        Category savedCategory = categoryRepository.save(category);

        logger.info("Category created successfully with id={}, name={}",
                savedCategory.getId(),
                savedCategory.getName()
        );

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        logger.info("Fetching all categories");

        List<Category> categories = categoryRepository.findAll();

        logger.info("Fetched {} categories", categories.size());

        return categories.stream().map(categoryMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        logger.info("Fetching category with id={}",
                id
        );

        Category category = getCategoryEntityById(id);

        logger.info("Fetched category with id={}",
                id
        );

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        logger.info("Updating category with id={}, name={}",
                id,
                request.getName());

        Category existingCategory = getCategoryEntityById(id);

        if (!existingCategory.getName().equals(request.getName())
                && categoryRepository.existsByName(request.getName())) {

            throw new CategoryAlreadyExistsException(
                    "Category '" + request.getName() + "' already exists."
            );
        }

        existingCategory.setName(request.getName());
        existingCategory.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(existingCategory);

        logger.info("Category with id={}, name={} updated",
                id,
                updatedCategory.getName()
        );

        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        logger.info("Category with id={} is being deleted",
                id
        );

        Category existingCategory = getCategoryEntityById(id);

        categoryRepository.delete(existingCategory);

        logger.info("Category deleted successfully. id={}",
                id
        );
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
