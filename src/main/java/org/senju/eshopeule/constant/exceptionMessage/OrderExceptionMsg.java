package org.senju.eshopeule.constant.exceptionMessage;

public final class OrderExceptionMsg {
    public static final String ORDER_NOT_FOUND_MSG = "Order not found";
    public static final String ORDER_NOT_FOUND_WITH_ID_MSG = "Order not found with ID: %s";
    public static final String ORDER_NOT_FOUND_WITH_ID_AND_USERNAME_MSG = "Order not found with ID [%s] and Username [%s]";
    public static final String NO_ACTIVE_CART_MSG = "No active cart available to proceed with the order";
    public static final String EMPTY_ACTIVE_CART_MSG = "Cart must not be empty";
    public static final String UNSUPPORTED_TRANSACTION_TYPE_MSG = "Transaction type not supported: %s";
    public static final String UNSUPPORTED_DELIVERY_METHOD_MSG = "Delivery method not supported: %s";
    public static final String CUSTOMER_PROFILE_INVALID_MSG = "Customer profile is invalid";
    public static final String ORDER_ITEM_NOT_FOUND_WITH_ID_MSG = "Order item not found with ID [%s]";
    public static final String ORDER_ITEM_NOT_FOUND_WITH_ID_AND_USERNAME_MSG = "Order item not found with ID [%s] and Username [%s]";
    public static final String ERROR_UPDATE_ORDER_STATUS = "Error update order status";
    public static final String NOT_ALLOWED_TO_ORDER = "Product not allowed to order with ID [%s]";
    public static final String NOT_ALLOWED_TO_CANCEL_ORDER = "Order not allowed to cancel with username or permission invalid";
}
