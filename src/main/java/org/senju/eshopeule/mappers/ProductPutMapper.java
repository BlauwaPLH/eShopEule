package org.senju.eshopeule.mappers;

import org.mapstruct.*;
import org.senju.eshopeule.dto.ProductPutDTO;
import org.senju.eshopeule.model.product.Brand;
import org.senju.eshopeule.model.product.Category;
import org.senju.eshopeule.model.product.Product;
import org.senju.eshopeule.model.product.ProductCategory;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ProductPutMapper implements ProductMapper<ProductPutDTO> {

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "brand", expression = "java(mappingBrand(dto))")
//    @Mapping(target = "productCategories", expression = "java(mappingCategories(dto))")
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "productOptions", ignore = true)
    public abstract Product convertToEntity(ProductPutDTO dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "brand", expression = "java(mappingBrand(dto))")
    @Mapping(target = "productCategories", expression = "java(mappingCategories(dto))")
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "productOptions", ignore = true)
    public abstract void updateProduct(ProductPutDTO dto, @MappingTarget Product product);

    @Override
    public final ProductPutDTO convertToDTO(Product entity) {return null;}

    protected Brand mappingBrand(ProductPutDTO dto) {
        return Brand.builder().id(dto.getBrandId()).build();
    }

    protected List<ProductCategory> mappingCategories(ProductPutDTO dto) {
        if (dto.getCategoryIds().isEmpty()) return null;
        return dto.getCategoryIds()
                .stream()
                .map(cid -> ProductCategory.builder()
                        .category(Category.builder().id(cid).build())
                        .build())
                .collect(Collectors.toList());
    }
}
