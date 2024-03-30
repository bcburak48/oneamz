package com.oneamz.inventory.controller;

import com.oneamz.inventory.model.Category;
import com.oneamz.inventory.model.Product;
import com.oneamz.inventory.repository.CategoryRepository;
import com.oneamz.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        Category electronics = categoryRepository.findByName("Electronics")
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName("Electronics");
                    return categoryRepository.save(newCategory);
                });
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(999.99);
        product.setQuantity(5);
        product.setCategory(electronics);
        productRepository.save(product);
    }

    @Test
    @WithMockUser(username="admin", roles="USER")
    void whenGetAllProducts_thenReturnJsonArray() throws Exception {
        // Set up
        Category electronics = categoryRepository.findByName("Electronics")
                .orElseThrow(() -> new IllegalStateException("Test setup error: Category not found."));
        Product testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(100.0);
        testProduct.setQuantity(10);
        testProduct.setCategory(electronics);
        productRepository.save(testProduct);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[1].name", is("Test Product")));
    }

    @Test
    @WithMockUser(username="admin", roles="USER")
    void whenGetTotalInventoryValue_thenReturnCorrectValue() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/total-value"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(is("4999.95")));
    }

}