package org.senju.eshopeule.mappers;

import org.mapstruct.*;
import org.senju.eshopeule.dto.BrandDTO;
import org.senju.eshopeule.model.product.Brand;

@Mapper(componentModel = "spring")
public interface BrandMapper extends BaseMapper<Brand, BrandDTO> {

    @Override
    @Mapping(target = "products", ignore = true)
    Brand convertToEntity(BrandDTO dto);

    @Override
    @Mapping(target = "createdOn", expression = "java(entity.getCreatedOn())")
    @Mapping(target = "createdBy", expression = "java(entity.getCreatedBy())")
    @Mapping(target = "lastModifiedOn", expression = "java(entity.getLastModifiedOn())")
    @Mapping(target = "lastModifiedBy", expression = "java(entity.getLastModifiedBy())")
    BrandDTO convertToDTO(Brand entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(BrandDTO dto, @MappingTarget Brand entity);
}
