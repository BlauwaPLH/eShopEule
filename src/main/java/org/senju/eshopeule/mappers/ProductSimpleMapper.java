package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.ProductSimpleDTO;
import org.senju.eshopeule.model.product.Product;

@Mapper(componentModel = "spring")
public abstract class ProductSimpleMapper implements ProductMapper<ProductSimpleDTO> {

    @Override
    public final Product convertToEntity(ProductSimpleDTO dto) {return null;}

    @Override
    @Mapping(target = "imageUrl", expression = "java(mappingImageUrl(entity))")
    public abstract ProductSimpleDTO convertToDTO(Product entity);

    protected String mappingImageUrl(Product entity) {
        if (entity.getProductImages() == null || entity.getProductImages().isEmpty()) return null;
        return entity.getProductImages().get(0).getImageUrl();
    }
}
