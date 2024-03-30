package com.oneamz.inventory.mapper;

import com.oneamz.inventory.dto.ProductDTO;
import com.oneamz.inventory.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product productDTOToProduct(ProductDTO productDTO);

    @Mapping(target = "categoryName", source = "category.name")
    ProductDTO productToProductDTOWithCategoryName(Product product);
}