package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.senju.eshopeule.dto.ProductMetaDTO;
import org.senju.eshopeule.model.product.ProductMeta;

@Mapper(componentModel = "spring")
public interface ProductMetaMapper extends BaseMapper<ProductMeta, ProductMetaDTO> {

}
