package com.oneamz.inventory.controller;

import com.oneamz.inventory.dto.ProductDTO;
import com.oneamz.inventory.mapper.ProductMapper;
import com.oneamz.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class InventoryController {
    private final Logger log = LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService inventoryService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        log.debug("GET /api/products - Request to get all products");
        List<ProductDTO> products = inventoryService.getAllProducts();
        log.info("GET /api/products - Returned {} products", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        log.debug("GET /api/products/{} - Request to get product by id", id);
        ProductDTO product = inventoryService.getProductById(id);
        log.info("GET /api/products/{} - Returned product details", id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        log.info("POST /api/products - Request to create a new product: {}", productDTO.getName());
        ProductDTO savedProduct = inventoryService.saveProduct(productDTO);
        log.info("POST /api/products - Successfully created product: {}", productDTO.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        log.info("PUT /api/products/{} - Request to update product", id);
        ProductDTO updatedProduct = inventoryService.saveProduct(productDTO);
        log.info("PUT /api/products/{} - Successfully updated product", id);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{} - Request to delete product", id);
        inventoryService.deleteProduct(id);
        log.info("DELETE /api/products/{} - Successfully deleted product", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-value")
    public ResponseEntity<Double> getTotalInventoryValue() {
        log.info("GET /api/products/total-value - Request to get total inventory value");
        double totalValue = inventoryService.calculateTotalInventoryValue();
        log.info("GET /api/products/total-value - Successfully calculated total inventory value: {}", totalValue);
        return ResponseEntity.ok(totalValue);
    }
}