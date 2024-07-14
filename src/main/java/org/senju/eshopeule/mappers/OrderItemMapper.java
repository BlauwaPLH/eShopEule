package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.senju.eshopeule.dto.OrderItemDTO;
import org.senju.eshopeule.dto.ProductAttributeValueDTO;
import org.senju.eshopeule.dto.ProductOptionDTO;
import org.senju.eshopeule.dto.ProductSimpleDTO;
import org.senju.eshopeule.model.order.OrderItem;
import org.senju.eshopeule.model.product.ProductOption;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class OrderItemMapper implements BaseMapper<OrderItem, OrderItemDTO> {

    @Override
    public final OrderItem convertToEntity(OrderItemDTO dto) {return null;}

    @Override
    @Mapping(target = "option", expression = "java(mappingOption(entity))")
    @Mapping(target = "product", expression = "java(mappingProduct(entity))")
    public abstract OrderItemDTO convertToDTO(OrderItem entity);

    protected ProductSimpleDTO mappingProduct(OrderItem entity) {
        if (entity.getProduct() == null) return null;
        return Mappers.getMapper(ProductSimpleMapper.class).convertToDTO(entity.getProduct());
    }

    protected ProductOptionDTO mappingOption(OrderItem entity) {
        if (entity.getOption() == null) return null;
        final ProductOption optionEntity = entity.getOption();
        return ProductOptionDTO.builder()
                .id(optionEntity.getId())
                .name(optionEntity.getName())
                .attributes(mappingOptionAttributes(optionEntity))
                .build();
    }

    protected Map<String, ProductAttributeValueDTO> mappingOptionAttributes(ProductOption option) {
        if (option.getProductAttributeValues() == null || option.getProductAttributeValues().isEmpty()) return null;
        return option.getProductAttributeValues().stream().collect(
                Collectors.toMap(
                        pav -> pav.getProductAttribute().getName(),
                        pav -> new ProductAttributeValueDTO(pav.getValue())
                )
        );
    }
}
