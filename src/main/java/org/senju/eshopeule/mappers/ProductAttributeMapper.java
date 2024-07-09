package org.senju.eshopeule.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.ProductAttributeDTO;
import org.senju.eshopeule.model.product.ProductAttribute;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper extends BaseMapper<ProductAttribute, ProductAttributeDTO> {

    @Override
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "productAttributeValues", ignore = true)
    ProductAttribute convertToEntity(ProductAttributeDTO dto);
}
