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
    public final Product convertToEntity(ProductPutDTO dto) {return null;};

    @Override
    public final ProductPutDTO convertToDTO(Product entity) {return null;}


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "brand", expression = "java(dto.getBrandId() != null ? mappingBrand(dto) : entity.getBrand())")
    @Mapping(target = "productCategories", expression = "java(dto.getCategoryIds() != null ? mappingCategories(dto) : entity.getProductCategories())")
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "productOptions", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedOn", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    public abstract void updateProductFromDTO(ProductPutDTO dto, @MappingTarget Product entity);

    protected Brand mappingBrand(ProductPutDTO dto) {
        if (dto.getBrandId().isBlank()) return null;
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
