package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.ProductPostDTO;
import org.senju.eshopeule.model.product.*;

@Mapper(componentModel = "spring")
public abstract class ProductPostMapper implements ProductMapper<ProductPostDTO> {


    @Override
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productOptions", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract Product convertToEntity(ProductPostDTO dto);

    @Override
    public final ProductPostDTO convertToDTO(Product entity) {return null;}

}
