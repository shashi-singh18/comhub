package com.shashi.comhub.controller;

import com.shashi.comhub.dto.CategoryRequest;
import com.shashi.comhub.dto.CategoryResponse;
import com.shashi.comhub.dto.common.PageResponse;
import com.shashi.comhub.exception.CategoryAlreadyExistsException;
import com.shashi.comhub.exception.CategoryNotFoundException;
import com.shashi.comhub.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CategoryService categoryService;

    CategoryRequest categoryRequest;
    CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Electronics");
        categoryRequest.setDescription("For electronic products.");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(2L);
        categoryResponse.setName("Electronics");
        categoryResponse.setDescription("For electronic products.");
    }

    @Test
    void shouldCreateCategory() throws Exception {

        when(categoryService.createCategory(any(CategoryRequest.class)))
                .thenReturn(categoryResponse);

        mockMvc.perform(
                        post("/api/v1/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Electronics",
                                          "description": "For electronic products."
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.description")
                        .value("For electronic products."));

        verify(categoryService)
                .createCategory(any(CategoryRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenCategoryNameIsBlank()
            throws Exception {

        mockMvc.perform(
                        post("/api/v1/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "",
                                          "description": "For electronic products."
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .createCategory(any(CategoryRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenCategoryDescriptionIsBlank()
            throws Exception {

        mockMvc.perform(
                        post("/api/v1/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Electronics",
                                          "description": ""
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .createCategory(any(CategoryRequest.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists()
            throws Exception {

        when(categoryService.createCategory(any(CategoryRequest.class)))
                .thenThrow(
                        new CategoryAlreadyExistsException(
                                "Category 'Electronics' already exists."
                        )
                );

        mockMvc.perform(
                        post("/api/v1/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Electronics",
                                          "description": "For electronic products."
                                        }
                                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Category 'Electronics' already exists."));

        verify(categoryService)
                .createCategory(any(CategoryRequest.class));
    }

    @Test
    void shouldReturnCategories() throws Exception {

        PageResponse<CategoryResponse> pageResponse =
                new PageResponse<>();

        pageResponse.setData(List.of(categoryResponse));
        pageResponse.setPage(0);
        pageResponse.setSize(20);
        pageResponse.setTotalElements(1);
        pageResponse.setTotalPages(1);
        pageResponse.setFirst(true);
        pageResponse.setLast(true);
        pageResponse.setHasNext(false);
        pageResponse.setHasPrevious(false);

        when(categoryService.getAllCategories(any(Pageable.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(
                        get("/api/v1/categories")
                                .param("page", "0")
                                .param("size", "20")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(2))
                .andExpect(jsonPath("$.data[0].name")
                        .value("Electronics"))
                .andExpect(jsonPath("$.data[0].description")
                        .value("For electronic products."))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(categoryService)
                .getAllCategories(any(Pageable.class));
    }

    @Test
    void shouldReturnEmptyCategories() throws Exception {

        PageResponse<CategoryResponse> pageResponse =
                new PageResponse<>();

        pageResponse.setData(List.of());
        pageResponse.setPage(0);
        pageResponse.setSize(20);
        pageResponse.setTotalElements(0);
        pageResponse.setTotalPages(0);
        pageResponse.setFirst(true);
        pageResponse.setLast(true);
        pageResponse.setHasNext(false);
        pageResponse.setHasPrevious(false);

        when(categoryService.getAllCategories(any(Pageable.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(
                        get("/api/v1/categories")
                                .param("page", "0")
                                .param("size", "20")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.hasPrevious").value(false));

        verify(categoryService)
                .getAllCategories(any(Pageable.class));
    }

    @Test
    void shouldReturnCategoryWhenCategoryExists()
            throws Exception {

        when(categoryService.getCategoryById(2L))
                .thenReturn(categoryResponse);

        mockMvc.perform(
                        get("/api/v1/categories/2")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name")
                        .value("Electronics"))
                .andExpect(jsonPath("$.description")
                        .value("For electronic products."));

        verify(categoryService)
                .getCategoryById(2L);
    }

    @Test
    void shouldReturnNotFoundWhenCategoryDoesNotExist()
            throws Exception {

        when(categoryService.getCategoryById(100L))
                .thenThrow(
                        new CategoryNotFoundException(
                                "Category not found with id=100"
                        )
                );

        mockMvc.perform(
                        get("/api/v1/categories/100")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Category not found with id=100"));

        verify(categoryService)
                .getCategoryById(100L);
    }

    @Test
    void shouldUpdateCategoryWhenCategoryExists()
            throws Exception {

        CategoryResponse updatedResponse =
                new CategoryResponse();

        updatedResponse.setId(2L);
        updatedResponse.setName("Gadgets");
        updatedResponse.setDescription(
                "Electronic gadgets and accessories."
        );

        when(categoryService.updateCategory(
                eq(2L),
                any(CategoryRequest.class)
        )).thenReturn(updatedResponse);

        mockMvc.perform(
                        put("/api/v1/categories/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Gadgets",
                                          "description": "Electronic gadgets and accessories."
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name")
                        .value("Gadgets"))
                .andExpect(jsonPath("$.description")
                        .value("Electronic gadgets and accessories."));

        verify(categoryService)
                .updateCategory(
                        eq(2L),
                        any(CategoryRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingWithBlankName()
            throws Exception {

        mockMvc.perform(
                        put("/api/v1/categories/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "",
                                          "description": "Electronic gadgets and accessories."
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .updateCategory(
                        anyLong(),
                        any(CategoryRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingWithBlankDescription()
            throws Exception {

        mockMvc.perform(
                        put("/api/v1/categories/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Gadgets",
                                          "description": ""
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .updateCategory(
                        anyLong(),
                        any(CategoryRequest.class)
                );
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingCategory()
            throws Exception {

        when(categoryService.updateCategory(
                eq(100L),
                any(CategoryRequest.class)
        )).thenThrow(
                new CategoryNotFoundException(
                        "Category not found with id=100"
                )
        );

        mockMvc.perform(
                        put("/api/v1/categories/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Gadgets",
                                          "description": "Electronic gadgets and accessories."
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Category not found with id=100"));

        verify(categoryService)
                .updateCategory(
                        eq(100L),
                        any(CategoryRequest.class)
                );
    }

    @Test
    void shouldReturnConflictWhenUpdatedCategoryNameAlreadyExists()
            throws Exception {

        when(categoryService.updateCategory(
                eq(2L),
                any(CategoryRequest.class)
        )).thenThrow(
                new CategoryAlreadyExistsException(
                        "Category 'Gadgets' already exists."
                )
        );

        mockMvc.perform(
                        put("/api/v1/categories/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Gadgets",
                                          "description": "Electronic gadgets and accessories."
                                        }
                                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Category 'Gadgets' already exists."));

        verify(categoryService)
                .updateCategory(
                        eq(2L),
                        any(CategoryRequest.class)
                );
    }

    @Test
    void shouldDeleteCategoryWhenCategoryExists()
            throws Exception {

        doNothing()
                .when(categoryService)
                .deleteCategory(2L);

        mockMvc.perform(
                        delete("/api/v1/categories/2")
                )
                .andExpect(status().isNoContent());

        verify(categoryService)
                .deleteCategory(2L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingCategory()
            throws Exception {

        doThrow(
                new CategoryNotFoundException(
                        "Category not found with id=100"
                )
        )
                .when(categoryService)
                .deleteCategory(100L);

        mockMvc.perform(
                        delete("/api/v1/categories/100")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Category not found with id=100"));

        verify(categoryService)
                .deleteCategory(100L);
    }
}