package com.shashi.comhub.service;

import com.shashi.comhub.dto.CategoryRequest;
import com.shashi.comhub.dto.CategoryResponse;
import com.shashi.comhub.dto.common.PageResponse;
import com.shashi.comhub.entity.Category;
import com.shashi.comhub.exception.CategoryAlreadyExistsException;
import com.shashi.comhub.exception.CategoryNotFoundException;
import com.shashi.comhub.mapper.CategoryMapper;
import com.shashi.comhub.mapper.PageMapper;
import com.shashi.comhub.repository.CategoryRepository;
import com.shashi.comhub.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private PageMapper pageMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    Category category;
    CategoryRequest categoryRequest;
    CategoryResponse categoryResponse;
    PageResponse<CategoryResponse> pageResponse;
    PageResponse<CategoryResponse> emptyPageResponse;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(10L);
        category.setName("Electronics");
        category.setDescription("For electronic products.");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Electronics");
        categoryRequest.setDescription("For electronic products.");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(10L);
        categoryResponse.setName("Electronics");
        categoryResponse.setDescription("For electronic products.");

        pageResponse = new PageResponse<>();
        pageResponse.setData(List.of(categoryResponse));
        pageResponse.setPage(0);
        pageResponse.setSize(10);
        pageResponse.setTotalElements(1);
        pageResponse.setTotalPages(1);
        pageResponse.setFirst(true);
        pageResponse.setLast(true);
        pageResponse.setHasNext(false);
        pageResponse.setHasPrevious(false);

        emptyPageResponse = new PageResponse<>();
        emptyPageResponse.setData(List.of());
        emptyPageResponse.setPage(0);
        emptyPageResponse.setSize(10);
        emptyPageResponse.setTotalElements(0);
        emptyPageResponse.setTotalPages(0);
        emptyPageResponse.setFirst(true);
        emptyPageResponse.setLast(true);
        emptyPageResponse.setHasNext(false);
        emptyPageResponse.setHasPrevious(false);
    }

    @Test
    void shouldCreateCategoryWhenCategoryNameDoesNotExists() {
        when(categoryRepository.existsByName(categoryRequest.getName())).thenReturn(false);
        when(categoryMapper.toEntity(categoryRequest)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        CategoryResponse response = categoryService.createCategory(categoryRequest);

        assertNotNull(response);
        assertEquals(categoryResponse, response);

        verify(categoryRepository).existsByName(categoryRequest.getName());
        verify(categoryMapper).toEntity(categoryRequest);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toResponse(category);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNameExists() {
        when(categoryRepository.existsByName(category.getName())).thenReturn(true);

        CategoryAlreadyExistsException exception =
                assertThrows(CategoryAlreadyExistsException.class,
                        () -> categoryService.createCategory(categoryRequest));

        assertEquals("Category '" + categoryRequest.getName() + "' already exists.",
                exception.getMessage());

        verify(categoryRepository).existsByName(categoryRequest.getName());
        verify(categoryMapper, never()).toEntity(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void shouldReturnCategoriesWhenExists() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> categoryPage =
                new PageImpl<>(
                        List.of(category),
                        pageable,
                        1
                );

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);
        when(pageMapper.toPageResponse(any(Page.class))).thenReturn(pageResponse);

        PageResponse<CategoryResponse> response = categoryService.getAllCategories(pageable);

        assertNotNull(response);
        assertEquals(pageResponse, response);

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toResponse(category);
        verify(pageMapper).toPageResponse(any(Page.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoryExists() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> emptyPage =
                new PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );

        when(categoryRepository.findAll(pageable)).thenReturn(emptyPage);
        when(pageMapper.toPageResponse(any(Page.class))).thenReturn(emptyPageResponse);

        PageResponse<CategoryResponse> response = categoryService.getAllCategories(pageable);

        assertNotNull(response);
        assertEquals(emptyPageResponse, response);

        verify(categoryRepository).findAll(pageable);
        verify(pageMapper).toPageResponse(any(Page.class));
    }

    @Test
    void shouldReturnCategoryWhenIdExists() {
        //Arrange
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        //Act
        CategoryResponse response = categoryService.getCategoryById(10L);

        //Assert
        assertNotNull(response);
        assertEquals(categoryResponse, response);

        verify(categoryRepository).findById(10L);
        verify(categoryMapper).toResponse(category);
    }

    @Test
    void shouldThrowExceptionWhenGettingCategoryDoesNotExist() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception =
                assertThrows(CategoryNotFoundException.class,
                        () -> categoryService.getCategoryById(100L));

        assertEquals("Category not found with id=" + 100L,
                exception.getMessage());

        verify(categoryRepository).findById(100L);
        verify(categoryMapper, never()).toResponse(any());
    }

    @Test
    void shouldUpdateCategoryWhenCategoryNameRemainsSame() {
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        CategoryResponse response = categoryService.updateCategory(10L, categoryRequest);

        assertNotNull(response);
        assertEquals(categoryResponse, response);

        verify(categoryRepository).findById(10L);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toResponse(category);
        verify(categoryRepository, never()).existsByName(any());
    }

    @Test
    void shouldUpdateCategoryWhenNewCategoryNameDoesNotAlreadyExist() {
        categoryRequest.setName("Gadgets");
        categoryRequest.setDescription("Electronic gadgets and accessories.");

        CategoryResponse updatedResponse = new CategoryResponse();
        updatedResponse.setId(10L);
        updatedResponse.setName("Gadgets");
        updatedResponse.setDescription("Electronic gadgets and accessories.");

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName("Gadgets")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(updatedResponse);

        CategoryResponse response = categoryService.updateCategory(10L, categoryRequest);

        assertNotNull(response);
        assertEquals(updatedResponse, response);

        verify(categoryRepository).findById(10L);
        verify(categoryRepository).existsByName("Gadgets");
        verify(categoryRepository).save(category);
        verify(categoryMapper).toResponse(category);
    }

    @Test
    void shouldThrowExceptionWhenRequestedCategoryNameAlreadyExists() {
        category.setName("Electronics");
        categoryRequest.setName("Fashion");

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(categoryRequest.getName())).thenReturn(true);

        CategoryAlreadyExistsException exception =
                assertThrows(CategoryAlreadyExistsException.class,
                        () -> categoryService.updateCategory(10L, categoryRequest));

        assertEquals("Category '" + categoryRequest.getName() + "' already exists.",
                exception.getMessage());

        verify(categoryRepository).findById(10L);
        verify(categoryRepository).existsByName(categoryRequest.getName());
        verify(categoryRepository, never()).save(category);
    }

    @Test
    void shouldThrowExceptionWhenRequestedCategoryDoesNotExist() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception =
                assertThrows(CategoryNotFoundException.class,
                        () -> categoryService.updateCategory(100L, categoryRequest));

        assertEquals("Category not found with id=" + 100L,
                exception.getMessage());

        verify(categoryRepository).findById(100L);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldDeleteCategoryWhenCategoryExists() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        categoryService.deleteCategory(category.getId());

        verify(categoryRepository).findById(category.getId());
        verify(categoryRepository).delete(category);
    }

    @Test
    void shouldThrowExceptionWhenDeletingCategoryDoesNotExist() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception =
                assertThrows(CategoryNotFoundException.class,
                        () -> categoryService.deleteCategory(100L));

        assertEquals("Category not found with id=" + 100L, exception.getMessage());

        verify(categoryRepository).findById(100L);
        verify(categoryRepository, never()).delete(any());
    }
}
