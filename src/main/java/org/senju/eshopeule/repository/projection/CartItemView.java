package org.senju.eshopeule.repository.projection;

public interface CartItemView {
    String getId();
    Integer getItemQuantity();
    String getProductId();
    String getOptionId();
    Double getPrice();
    Double getDiscount();
    Long getProductQuantity();
}
