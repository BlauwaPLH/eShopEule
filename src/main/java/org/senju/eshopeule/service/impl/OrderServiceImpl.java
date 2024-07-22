package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.OrderDTO;
import org.senju.eshopeule.dto.request.CreateOrderRequest;
import org.senju.eshopeule.dto.response.OrderPagingResponse;
import org.senju.eshopeule.exceptions.CartException;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.OrderException;
import org.senju.eshopeule.mappers.OrderMapper;
import org.senju.eshopeule.model.cart.Cart;
import org.senju.eshopeule.model.cart.CartItem;
import org.senju.eshopeule.model.cart.CartStatus;
import org.senju.eshopeule.model.order.*;
import org.senju.eshopeule.model.product.Product;
import org.senju.eshopeule.model.product.ProductOption;
import org.senju.eshopeule.model.user.Customer;
import org.senju.eshopeule.repository.jpa.*;
import org.senju.eshopeule.repository.projection.CartItemQuantityView;
import org.senju.eshopeule.repository.projection.OrderItemView;
import org.senju.eshopeule.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.senju.eshopeule.constant.enums.BootstrapPerm.CUS_WRITE;
import static org.senju.eshopeule.constant.enums.BootstrapPerm.STAFF_WRITE;
import static org.senju.eshopeule.constant.exceptionMessage.CartExceptionMsg.ONLY_ONE_ACTIVE_CART_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.CartExceptionMsg.QUANTITY_EXCEEDED_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.CustomerExceptionMsg.CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.OrderExceptionMsg.*;
import static org.senju.eshopeule.model.order.OrderStatus.*;
import static org.senju.eshopeule.model.order.TransactionType.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper mapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;

    @Override
    public OrderDTO getOrderDetail(String orderId) {
        return mapper.convertToDTO(orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException(String.format(ORDER_NOT_FOUND_WITH_ID_MSG, orderId))
        ));
    }

    @Override
    public OrderPagingResponse getAllOrder(Pageable pageRequest) {
        return getOrderPaging(orderRepository.findAll(pageRequest), mapper);
    }

    @Override
    public OrderPagingResponse getOrderHistory(Pageable pageRequest) {
        final String username = this.getCurrentUsername();
        return getOrderPaging(orderRepository.getAllOrderWithUsername(username, pageRequest), mapper);
    }

    private OrderPagingResponse getOrderPaging(Page<Order> orderPage, OrderMapper mapper) {
        return new OrderPagingResponse(
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.getPageable().getPageNumber() + 1,
                orderPage.getPageable().getPageSize(),
                orderPage.isLast(),
                orderPage.getContent().stream()
                        .map(mapper::convertToDTO)
                        .toList()
        );
    }

    @Override
    @Transactional
    public void createOrder(CreateOrderRequest request) {
        final Map<String, TransactionType> transactionTypeMap = Arrays.stream(TransactionType.values())
                .collect(Collectors.toMap(TransactionType::name, Function.identity()));
        final Map<String, DeliveryMethod> deliveryMethodMap = Arrays.stream(DeliveryMethod.values())
                .collect(Collectors.toMap(DeliveryMethod::name, Function.identity()));

        final TransactionType transactionType;
        final DeliveryMethod deliveryMethod;

        if (transactionTypeMap.containsKey(request.getTransactionType())) transactionType = transactionTypeMap.get(request.getTransactionType());
        else throw new OrderException(String.format(UNSUPPORTED_TRANSACTION_TYPE_MSG, request.getTransactionType()));

        if (deliveryMethodMap.containsKey(request.getDeliveryMethod())) deliveryMethod = deliveryMethodMap.get(request.getDeliveryMethod());
        else throw new OrderException(String.format(UNSUPPORTED_DELIVERY_METHOD_MSG, request.getDeliveryMethod()));

        List<Cart> activeCartList = this.getActiveCartsOfCurrentUser();
        if (activeCartList.isEmpty())
            throw new OrderException(NO_ACTIVE_CART_MSG);

        final Cart activeCart = activeCartList.get(0);
        if (activeCart.getItems() == null || activeCart.getItems().isEmpty())
            throw new OrderException(EMPTY_ACTIVE_CART_MSG);

        final Customer profile = activeCart.getCustomer();
        final Order newOrder = Order.builder()
                .status(PROCESSING)
                .deliveryMethod(deliveryMethod)
                .customer(profile)
                .cart(activeCart)
                .build();

        if (profile.getFirstName() != null && profile.getLastName() != null
                && profile.getPhoneNumber() != null && profile.getAddress() != null) {
            newOrder.setContactName(profile.getFullName());
            newOrder.setAddress(profile.getAddress());
            newOrder.setPhoneNumber(profile.getPhoneNumber());
        } else throw new OrderException(CUSTOMER_PROFILE_INVALID_MSG);

        final Transaction newTransaction = Transaction.builder()
                .type(transactionType)
                .order(newOrder)
                .build();
        newOrder.setTransaction(newTransaction);

        final List<OrderItem> orderItemList = cartItemRepository.getItemViewByCartId(activeCart.getId())
                .stream()
                .map(civ -> {
                    if (!productRepository.checkAllowedToOrder(civ.getProductId())) {
                        throw new OrderException(String.format(NOT_ALLOWED_TO_ORDER, civ.getProductId()));
                    }
                    if (civ.getItemQuantity() > civ.getProductQuantity()) {
                        throw new OrderException(QUANTITY_EXCEEDED_MSG);
                    }
                    return OrderItem.builder()
                            .order(newOrder)
                            .option(civ.getOptionId() != null ? ProductOption.builder().id(civ.getOptionId()).build() : null)
                            .product(Product.builder().id(civ.getProductId()).build())
                            .quantity(civ.getItemQuantity())
                            .total(calculateTotal(civ.getPrice(), civ.getDiscount(), civ.getItemQuantity()))
                            .build();
                })
                .toList();

        newOrder.setItems(orderItemList);
        newOrder.setTotal(orderItemList.stream()
                .map(OrderItem::getTotal)
                .reduce(Double::sum)
                .orElseThrow(() -> new OrderException("Error calculate order price")));

        orderRepository.save(newOrder);
        cartRepository.updateCompletedCartWithUsername(this.getCurrentUsername());
    }

    @Override
    @Transactional
    public void buyAgainOrderItem(String orderItemId) {
        final String username = this.getCurrentUsername();
        if (!orderItemRepository.checkExistsByUsername(orderItemId, username)) {
            throw new NotFoundException(String.format(ORDER_ITEM_NOT_FOUND_WITH_ID_AND_USERNAME_MSG, orderItemId, username));
        }

        OrderItemView orderItemView = orderItemRepository.getItemViewById(orderItemId).orElseThrow(
                () -> new NotFoundException(String.format(ORDER_ITEM_NOT_FOUND_WITH_ID_MSG, orderItemId))
        );

        if (!productRepository.checkAllowedToOrder(orderItemView.getProductId())) {
            throw new OrderException(String.format(NOT_ALLOWED_TO_ORDER, orderItemView.getProductId()));
        }

        List<Cart> activeCartList = this.getActiveCartsOfCurrentUser();
        final Cart activeCart;
        if (activeCartList.isEmpty()) {
            final String customerId = customerRepository.findIdByUsername(username).orElseThrow(
                    () -> new NotFoundException(String.format(CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG, username))
            );
            activeCart = Cart.builder()
                    .status(CartStatus.ACTIVE)
                    .customer(Customer.builder().id(customerId).build())
                    .build();
        } else {
            activeCart = activeCartList.get(0);
        }


        if (activeCart.getId() != null) {
            CartItemQuantityView existedCartItem = cartItemRepository
                    .getItemQuantityView(activeCart.getId(), orderItemView.getProductId(), orderItemView.getOptionId())
                    .orElse(null);

            if (existedCartItem != null) {
                if (existedCartItem.getQuantity() + 1 > orderItemView.getProductQuantity()) {
                    throw new CartException(QUANTITY_EXCEEDED_MSG);
                }
                cartItemRepository.updateQuantityById(existedCartItem.getId(), existedCartItem.getQuantity() + 1);
            } else {
                CartItem newCartItem = this.createCartItem(activeCart, orderItemView.getProductId(), orderItemView.getOptionId());
                activeCart.getItems().add(newCartItem);
                cartItemRepository.save(newCartItem);
            }
        } else {
            CartItem newCartItem = this.createCartItem(activeCart, orderItemView.getProductId(), orderItemView.getOptionId());
            activeCart.setItems(List.of(newCartItem));
        }

        cartRepository.save(activeCart);
    }


    private CartItem createCartItem(Cart activeCart, String productId, String optionId) {
        return CartItem.builder()
                .quantity(1)
                .option(optionId != null ? ProductOption.builder().id(optionId).build() : null)
                .product(Product.builder().id(productId).build())
                .cart(activeCart)
                .build();
    }

    @Override
    @Transactional
    public void updateCompletedOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException(String.format(ORDER_NOT_FOUND_WITH_ID_MSG, orderId))
        );
        if (order.getStatus().equals(SHIPPING)) {
            Transaction transaction = order.getTransaction();
            if (transaction.getType().equals(COD)) {
                transaction.setStatus(TransactionStatus.COMPLETED);
                transactionRepository.save(transaction);
            }
            order.setStatus(COMPLETED);
            orderRepository.save(order);
        } else throw new OrderException(ERROR_UPDATE_ORDER_STATUS);
    }

    @Override
    @Transactional
    public void updateShippingOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException(String.format(ORDER_NOT_FOUND_WITH_ID_MSG, orderId))
        );
        if (order.getStatus().equals(PROCESSING)) {
            Transaction transaction = order.getTransaction();
            if (transaction.getType().equals(TransactionType.BANKING)) {
                transaction.setStatus(TransactionStatus.COMPLETED);
                transactionRepository.save(transaction);
            }
            order.getItems().forEach(
                    i -> {
                        Product product = i.getProduct();
                        long newProductQuantity = product.getQuantity() - i.getQuantity();
                        if (newProductQuantity < 0) {
                            throw new OrderException();
                        } else {
                            product.setQuantity(newProductQuantity);
                            productRepository.save(product);
                        }
                    }
            );
            order.setStatus(SHIPPING);
            orderRepository.save(order);
        } else throw new OrderException(ERROR_UPDATE_ORDER_STATUS);
    }

    @Override
    @Transactional
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException(String.format(ORDER_NOT_FOUND_WITH_ID_MSG, orderId))
        );

        List<String> permissions = this.getCurrentPermission();
        if (permissions.contains(CUS_WRITE.getPermName())) {
            final String username = this.getCurrentUsername();
            if (!orderRepository.checkExistsByUsername(orderId, username)) {
                throw new NotFoundException(String.format(ORDER_NOT_FOUND_WITH_ID_AND_USERNAME_MSG, orderId, username));
            }
        } else if (!permissions.contains(STAFF_WRITE.getPermName())) {
            throw new OrderException(NOT_ALLOWED_TO_CANCEL_ORDER);
        }

        Transaction transaction = order.getTransaction();
        switch (order.getStatus()) {
            case PROCESSING -> {
                transaction.setStatus(TransactionStatus.CANCELLED);
                order.setStatus(CANCELLED);
            }
            case SHIPPING -> {
                transaction.setStatus(TransactionStatus.CANCELLED);
                order.setStatus(CANCELLED);
                order.getItems().forEach(
                        i -> {
                            Product product = i.getProduct();
                            product.setQuantity(product.getQuantity() + i.getQuantity());
                            productRepository.save(product);
                        }
                );
            }
            default -> throw new OrderException(ERROR_UPDATE_ORDER_STATUS);
        }
        transactionRepository.save(transaction);
        orderRepository.save(order);
    }

    private Double calculateTotal(Double price, Double discount, Integer quantity) {
        if (discount != null) return price * quantity.doubleValue() * (1.0 - discount / 100.0);
        return price * quantity.doubleValue();
    }


    private List<Cart> getActiveCartsOfCurrentUser() {
        final String username = this.getCurrentUsername();
        List<Cart> activeCartList = cartRepository.getActiveCartByUsername(username);

        if (activeCartList.size() > 1)
            throw new CartException(ONLY_ONE_ACTIVE_CART_MSG);
        return activeCartList;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @SuppressWarnings("unchecked")
    private List<String> getCurrentPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();
        return authorities.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();
    }
}
