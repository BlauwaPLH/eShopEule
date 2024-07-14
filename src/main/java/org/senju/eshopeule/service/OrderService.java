package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.OrderDTO;
import org.senju.eshopeule.dto.request.CreateOrderRequest;
import org.senju.eshopeule.dto.response.OrderPagingResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDTO getOrderDetail(String orderId);

    OrderPagingResponse getAllOrder(Pageable pageRequest);

    OrderPagingResponse getOrderHistory(Pageable pageRequest);

    void createOrder(CreateOrderRequest request);

    void buyAgainOrderItem(String orderItemId);

    void updateCompletedOrder(String orderId);

    void updateShippingOrder(String orderId);

    void cancelOrder(String orderId);
}
