package com.shashi.comhub.repository;

import com.shashi.comhub.entity.Category;
import com.shashi.comhub.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void shouldReturnTrueIfProductExists() {

        Category category = new Category();
        category.setName("Sports");
        category.setDescription("Sports equipment products.");

        Category savedCategory = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Cricket Bat");
        product.setDescription("English willow bat for cricket");
        product.setPrice(new BigDecimal("14599.00"));
        product.setBrand("MRF");
        product.setImageUrl(
                "https://images.comhub.com/products/cricket-bat-mrf.jpg"
        );
        product.setStock(10L);
        product.setActive(true);
        product.setCategory(savedCategory);

        productRepository.save(product);

        boolean productExists =
                productRepository.existsByCategoryId(savedCategory.getId());

        assertTrue(productExists);
    }

    @Test
    void shouldReturnFalseIfProductDoesNotExist() {

        boolean productExists =
                productRepository.existsByCategoryId(999L);

        assertFalse(productExists);
    }
}
