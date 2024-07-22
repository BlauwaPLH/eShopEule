package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.ProductSimpleDTO;
import org.senju.eshopeule.model.product.ProductESDoc;

@Mapper(componentModel = "spring")
public abstract class ProductSearchMapper implements BaseMapper<ProductESDoc, ProductSimpleDTO> {

    @Override
    public final ProductESDoc convertToEntity(ProductSimpleDTO dto) {return null;}

    @Override
    @Mapping(target = "id", source = "productId")
    public abstract ProductSimpleDTO convertToDTO(ProductESDoc entity);
}
