package com.oneamz.inventory.repository;

import com.oneamz.inventory.model.Category;
import com.oneamz.inventory.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Create and save a Category
        Category category = new Category();
        category.setName("Electronics");
        category = entityManager.persistFlushFind(category); // Save and immediately find to ensure it's managed

        // Create a Product with the Category
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(999.99);
        product.setQuantity(5);
        product.setCategory(category); // Associate the Category with the Product
        entityManager.persist(product);
        entityManager.flush(); // Ensure changes are persisted
    }

    @Test
    void whenFindById_thenReturnProduct() {
        // given
        Category category = entityManager.find(Category.class,1);
        // If you don't have the category ID, you can fetch it again from the database,
        // but since you've just inserted it in setUp, reusing it directly is more efficient.

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setQuantity(10);
        product.setCategory(category); // Associate the Product with the Category
        entityManager.persist(product);
        entityManager.flush();

        // when
        Product found = productRepository.findById(product.getId()).orElse(null);

        // then
        assertNotNull(found);
        assertEquals(product.getName(), found.getName());
    }
}