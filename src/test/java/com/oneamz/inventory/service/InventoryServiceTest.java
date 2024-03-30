package com.oneamz.inventory.service;

import com.oneamz.inventory.dto.ProductDTO;
import com.oneamz.inventory.mapper.ProductMapper;
import com.oneamz.inventory.model.Product;
import com.oneamz.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);
    }

    @Test
    void whenGetAllProducts_thenReturnProductList() {
        // given
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setQuantity(10);

        List<Product> productList = Collections.singletonList(product);

        when(productRepository.findAll()).thenReturn(productList);

        // when
        List<ProductDTO> products = inventoryService.getAllProducts();

        // then
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals(product.getName(), products.getFirst().getName());
    }
}