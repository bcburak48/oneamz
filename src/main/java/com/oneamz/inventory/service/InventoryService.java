package com.oneamz.inventory.service;

import com.oneamz.inventory.exception.ResourceNotFoundException;
import com.oneamz.inventory.dto.ProductDTO;
import com.oneamz.inventory.mapper.ProductMapper;
import com.oneamz.inventory.model.Category;
import com.oneamz.inventory.model.Product;
import com.oneamz.inventory.repository.CategoryRepository;
import com.oneamz.inventory.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    public List<ProductDTO> getAllProducts() {
        log.debug("Fetching all products from the database...");
        List<Product> products = productRepository.findAll();
        log.info("Fetched {} products from the database", products.size());
        return products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        log.debug("Attempting to find product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product with id {} not found.", id);
                    return new ResourceNotFoundException(String.format("Product with id %d not found", id));
                });
        log.info("Product with id {} found: {}", id, product.getName());
        return productMapper.productToProductDTO(product);
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        log.debug("Saving product to the database: {}", productDTO.getName());
        Category category = categoryRepository.findByName(productDTO.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(productDTO.getCategoryName());
                    return categoryRepository.save(newCategory);
                });
        Product product = productMapper.productDTOToProduct(productDTO);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        log.info("Product saved with id: {}", savedProduct.getId());
        return productMapper.productToProductDTOWithCategoryName(savedProduct);
    }

    public void deleteProduct(Long id) {
        log.debug("Attempting to delete product with id: {}", id);
        boolean exists = productRepository.existsById(id);
        if (!exists) {
            log.warn("Cannot delete product with id {}. Product not found.", id);
            throw new ResourceNotFoundException(String.format("Product with id %d not found", id));
        }
        productRepository.deleteById(id);
        log.info("Product with id {} deleted.", id);
    }

    public double calculateTotalInventoryValue() {
        log.debug("Calculating total inventory value.");
        double totalValue = productRepository.findAll().stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
        log.info("Total inventory value calculated: ${}", totalValue);
        return totalValue;
    }
}