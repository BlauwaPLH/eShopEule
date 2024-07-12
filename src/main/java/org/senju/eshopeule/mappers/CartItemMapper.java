package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.senju.eshopeule.dto.*;
import org.senju.eshopeule.model.cart.CartItem;
import org.senju.eshopeule.model.product.Product;
import org.senju.eshopeule.model.product.ProductOption;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CartItemMapper implements BaseMapper<CartItem, CartItemDTO> {

    @Override
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "option", expression = "java(mappingOption(dto))")
    @Mapping(target = "product", expression = "java(mappingProduct(dto))")
    public abstract CartItem convertToEntity(CartItemDTO dto);

    @Override
    @Mapping(target = "product", expression = "java(mappingProduct(entity))")
    @Mapping(target = "option", expression = "java(mappingOption(entity))")
    public abstract CartItemDTO convertToDTO(CartItem entity);

    protected Product mappingProduct(CartItemDTO dto) {
        if (dto.getProduct() == null) return null;
        return Product.builder().id(dto.getProduct().getId()).build();
    }

    protected ProductOption mappingOption(CartItemDTO dto) {
        if (dto.getOption() == null) return null;
        return ProductOption.builder().id(dto.getOption().getId()).build();
    }

    protected ProductSimpleDTO mappingProduct(CartItem entity) {
        if (entity.getProduct() == null) return null;
        return Mappers.getMapper(ProductSimpleMapper.class).convertToDTO(entity.getProduct());
    }


    protected ProductOptionDTO mappingOption(CartItem entity) {
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
