package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.ProductPostDTO;
import org.senju.eshopeule.model.product.*;
import org.senju.eshopeule.repository.jpa.ProductAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ProductPostMapper implements ProductMapper<ProductPostDTO> {

    @Autowired
    private ProductAttributeRepository attributeRepository;

    @Override
    @Mapping(target = "brand", expression = "java(mappingBrand(dto))")
    @Mapping(target = "productCategories", expression = "java(mappingCategories(dto))")
    @Mapping(target = "productOptions", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    public abstract Product convertToEntity(ProductPostDTO dto);

    @Override
    public final ProductPostDTO convertToDTO(Product entity) {return null;}

    protected Brand mappingBrand(ProductPostDTO dto) {
        return Brand.builder().id(dto.getBrandId()).build();
    }

    protected List<ProductCategory> mappingCategories(ProductPostDTO dto) {
        if (dto.getCategoryIds().isEmpty()) return null;
        return dto.getCategoryIds()
                .stream()
                .map(cid -> ProductCategory.builder()
                        .category(Category.builder().id(cid).build())
                        .build())
                .collect(Collectors.toList());
    }

}
