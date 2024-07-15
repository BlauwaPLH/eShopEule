package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.senju.eshopeule.dto.CartDTO;
import org.senju.eshopeule.dto.CartItemDTO;
import org.senju.eshopeule.model.cart.Cart;

import java.util.*;

@Mapper(componentModel = "spring")
public abstract class CartMapper implements BaseMapper<Cart, CartDTO> {

    @Override
    public final Cart convertToEntity(CartDTO dto) {return null;};

    @Override
    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "customerId", source = "entity.customer.id")
    @Mapping(target = "items", expression = "java(mappingItems(entity))")
    public abstract CartDTO convertToDTO(Cart entity);

    protected List<CartItemDTO> mappingItems(Cart entity) {
        if (entity.getItems() == null || entity.getItems().isEmpty()) return new ArrayList<>();
        return entity.getItems().stream()
                .map(Mappers.getMapper(CartItemMapper.class)::convertToDTO)
                .toList();
    }
}
