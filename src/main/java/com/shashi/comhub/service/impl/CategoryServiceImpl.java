package com.shashi.comhub.service.impl;

import com.shashi.comhub.dto.CategoryRequest;
import com.shashi.comhub.dto.CategoryResponse;
import com.shashi.comhub.dto.common.PageResponse;
import com.shashi.comhub.entity.Category;
import com.shashi.comhub.exception.CategoryAlreadyExistsException;
import com.shashi.comhub.exception.CategoryNotFoundException;
import com.shashi.comhub.mapper.CategoryMapper;
import com.shashi.comhub.mapper.PageMapper;
import com.shashi.comhub.repository.CategoryRepository;
import com.shashi.comhub.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PageMapper pageMapper;
    private static final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper,
                               PageMapper pageMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.pageMapper = pageMapper;
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
    public PageResponse<CategoryResponse> getAllCategories(Pageable pageable) {
        logger.info(
                "Fetching categories. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Category> categories = categoryRepository.findAll(pageable);

        logger.info("Fetched page {} containing {} categories out of {} total categories.",
                categories.getNumber(),
                categories.getNumberOfElements(),
                categories.getTotalElements());

        Page<CategoryResponse> categoryResponse = categories.map(categoryMapper::toResponse);

        return pageMapper.toPageResponse(categoryResponse);
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

            logger.warn(
                    "Cannot update category id={}. Category name '{}' already exists.",
                    id,
                    request.getName()
            );

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
