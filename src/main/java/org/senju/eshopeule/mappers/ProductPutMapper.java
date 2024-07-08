package org.senju.eshopeule.mappers;

import org.mapstruct.*;
import org.senju.eshopeule.dto.ProductPutDTO;
import org.senju.eshopeule.model.product.Product;

@Mapper(componentModel = "spring")
public abstract class ProductPutMapper implements ProductMapper<ProductPutDTO> {

    @Override
    public final Product convertToEntity(ProductPutDTO dto) {return null;};

    @Override
    public final ProductPutDTO convertToDTO(Product entity) {return null;}


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "productOptions", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedOn", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    public abstract void updateProductFromDTO(ProductPutDTO dto, @MappingTarget Product entity);

}
