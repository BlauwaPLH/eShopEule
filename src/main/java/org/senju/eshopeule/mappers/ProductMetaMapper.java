package org.senju.eshopeule.mappers;

import org.mapstruct.*;
import org.senju.eshopeule.dto.ProductMetaDTO;
import org.senju.eshopeule.model.product.ProductMeta;

@Mapper(componentModel = "spring")
public interface ProductMetaMapper extends BaseMapper<ProductMeta, ProductMetaDTO> {

    @Override
    ProductMeta convertToEntity(ProductMetaDTO dto);

    @Override
    ProductMetaDTO convertToDTO(ProductMeta entity);

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedOn", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(ProductMetaDTO dto, @MappingTarget ProductMeta entity);
}
