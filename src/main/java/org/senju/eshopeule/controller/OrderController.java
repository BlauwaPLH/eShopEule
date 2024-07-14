package org.senju.eshopeule.controller;

import co.elastic.clients.elasticsearch.xpack.usage.Base;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.pagination.OrderPageable;
import org.senju.eshopeule.constant.pagination.ProductPageable;
import org.senju.eshopeule.dto.request.CreateOrderRequest;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.CartException;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.OrderException;
import org.senju.eshopeule.exceptions.PagingException;
import org.senju.eshopeule.model.cart.Cart;
import org.senju.eshopeule.service.OrderService;
import org.senju.eshopeule.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static jakarta.mail.event.FolderEvent.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/r/v1/order")
public class OrderController {

    private OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @GetMapping
    @Operation(summary = "Get order detail with ID")
    public ResponseEntity<? extends BaseResponse> getOrderDetailById(@RequestParam("id") String orderId) {
        logger.info("Get order detail with ID {}", orderId);
        try {
            return ResponseEntity.ok(orderService.getOrderDetail(orderId));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/history")
    @Operation(summary = "Get order history of current customer")
    public ResponseEntity<? extends BaseResponse> getMyOrderHistory(
            @RequestParam(name = "pageNo", required = false, defaultValue = OrderPageable.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = OrderPageable.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = OrderPageable.DEFAULT_SORT_FIELD) String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = OrderPageable.DEFAULT_SORT_DIRECTION) String sortDirection
    ) {
        logger.info("Get order history of current customer");
        try {
            return ResponseEntity.ok(orderService.getOrderHistory(
                    PaginationUtil.findPaginated(pageNo, pageSize, sortField, sortDirection)
            ));
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/m/all")
    @Operation(summary = "Get all orders")
    public ResponseEntity<? extends BaseResponse> getAllOrders(
            @RequestParam(name = "pageNo", required = false, defaultValue = OrderPageable.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = OrderPageable.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = OrderPageable.DEFAULT_SORT_FIELD) String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = OrderPageable.DEFAULT_SORT_DIRECTION) String sortDirection
    ) {
        logger.info("Get all orders");
        try {
            return ResponseEntity.ok(orderService.getAllOrder(
                    PaginationUtil.findPaginated(pageNo, pageSize, sortField, sortDirection)
            ));
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }



    @PostMapping(path = "/crt")
    @Operation(summary = "Create new order")
    public ResponseEntity<? extends BaseResponse> createNewOrder(@Valid @RequestBody CreateOrderRequest request) {
        logger.info("Create new order");
        try {
            orderService.createOrder(request);
            return ResponseEntity.status(CREATED).body(new SimpleResponse("Created order successfully!"));
        } catch (NotFoundException | OrderException | CartException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/buy-again")
    @Operation(summary = "Buy again a order item")
    public ResponseEntity<? extends BaseResponse> buyAgainOrderItem(@RequestParam("id") String orderItemId) {
        logger.info("Buy again with order {}", orderItemId);
        try {
            orderService.buyAgainOrderItem(orderItemId);
            return ResponseEntity.ok(new SimpleResponse("Added products into cart"));
        } catch (NotFoundException | OrderException | CartException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping(path = "/m/complete")
    @Operation(summary = "Update completed order with ID")
    public ResponseEntity<? extends BaseResponse> updateCompletedOrder(@RequestParam("id") String orderId) {
        logger.info("Update completed order with ID: {}", orderId);
        try {
            orderService.updateCompletedOrder(orderId);
            return ResponseEntity.ok(new SimpleResponse("Update completed order successfully"));
        } catch (NotFoundException | OrderException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping(path = "/m/ship")
    @Operation(summary = "Update shipping order with ID")
    public ResponseEntity<? extends BaseResponse> updateShippingOrder(@RequestParam("id") String orderId) {
        logger.info("Update shipping order with ID: {}", orderId);
        try {
            orderService.updateShippingOrder(orderId);
            return ResponseEntity.ok(new SimpleResponse("Update shipping order successfully"));
        } catch (NotFoundException | OrderException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping(path = "/cancel")
    @Operation(summary = "Cancel order with ID")
    public ResponseEntity<? extends BaseResponse> cancelOrder(@RequestParam("id") String orderId) {
        logger.info("Cancel order with ID: {}", orderId);
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok(new SimpleResponse("Cancel order successfully"));
        } catch (NotFoundException | OrderException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }
}
