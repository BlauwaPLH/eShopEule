package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.senju.eshopeule.dto.OrderDTO;
import org.senju.eshopeule.dto.OrderItemDTO;
import org.senju.eshopeule.model.order.Order;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrderMapper implements BaseMapper<Order, OrderDTO>{


    @Override
    public final Order convertToEntity(OrderDTO dto) {return null;}

    @Override
    @Mapping(target = "status", source = "entity.status.name")
    @Mapping(target = "deliveryMethod", source = "entity.deliveryMethod.name")
    @Mapping(target = "transactionType", expression = "java(mappingTransactionType(entity))")
    @Mapping(target = "items", expression = "java(mappingItems(entity))")
    public abstract OrderDTO convertToDTO(Order entity);

    protected String mappingTransactionType(Order entity) {
        if (entity.getTransaction() == null) return null;
        return entity.getTransaction().getType().getName();
    }

    protected List<OrderItemDTO> mappingItems(Order entity) {
        if (entity.getItems() == null || entity.getItems().isEmpty()) return Collections.emptyList();
        return entity.getItems().stream()
                .map(Mappers.getMapper(OrderItemMapper.class)::convertToDTO)
                .toList();
    }
}
