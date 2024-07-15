package org.senju.eshopeule.repository.projection;

public interface OrderItemView {
    String getItemId();
    String getProductId();
    Long getProductQuantity();
    Double getPrice();
    Double getDiscount();
    String getOptionId();
}
