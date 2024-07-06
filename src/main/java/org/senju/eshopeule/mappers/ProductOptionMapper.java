package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.ProductAttributeValueDTO;
import org.senju.eshopeule.dto.ProductOptionDTO;
import org.senju.eshopeule.model.product.Product;
import org.senju.eshopeule.model.product.ProductAttribute;
import org.senju.eshopeule.model.product.ProductAttributeValue;
import org.senju.eshopeule.model.product.ProductOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ProductOptionMapper extends BaseMapper<ProductOption, ProductOptionDTO> {

    @Override
    @Mapping(target = "productId", source = "entity.product.id")
    @Mapping(target = "attributes", expression = "java(mappingAttributes(entity))")
    ProductOptionDTO convertToDTO(ProductOption entity);

    @Override
    @Mapping(target = "product", expression = "java(mappingProduct(dto))")
    @Mapping(target = "productAttributeValues", expression = "java(mappingAttributes(dto))")
    ProductOption convertToEntity(ProductOptionDTO dto);

    default Map<String, ProductAttributeValueDTO> mappingAttributes(ProductOption entity) {
        if (entity.getProductAttributeValues() == null) return null;
        final Map<String, ProductAttributeValueDTO> target = new HashMap<>();
        entity.getProductAttributeValues().forEach(
              pav -> target.put(
                      pav.getProductAttribute().getName(),
                      new ProductAttributeValueDTO(pav.getId(), pav.getValue())
              )
        );
        return target;
    }

    default List<ProductAttributeValue> mappingAttributes(ProductOptionDTO dto) {
        List<ProductAttributeValue> target = new ArrayList<>();
        dto.getAttributes().forEach(
                (attr, val) -> {
                    ProductAttribute pa = ProductAttribute.builder().name(attr).build();
                    ProductAttributeValue pav = ProductAttributeValue.builder()
                            .id(val.getId())
                            .value(val.getValue())
                            .productAttribute(pa)
                            .build();
                    target.add(pav);
                }
        );
        return target;
    }

    default Product mappingProduct(ProductOptionDTO dto) {
        return Product.builder().id(dto.getProductId()).build();
    }
}
