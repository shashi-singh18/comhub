package com.shashi.comhub.repository;

import com.shashi.comhub.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void shouldReturnTrueIfCategoryExistsByName() {
        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("For electronic products");

        categoryRepository.save(category);

        boolean categoryExists =
                categoryRepository.existsByName("Electronics");

        assertTrue(categoryExists);
    }

    @Test
    void shouldReturnFalseIfCategoryDoesNotExistByName() {
        boolean categoryExists =
                categoryRepository.existsByName("Electronics");

        assertFalse(categoryExists);
    }
}