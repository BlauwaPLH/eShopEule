package org.senju.eshopeule.repository.projection;

public interface CartItemView {
    String getId();
    Integer getQuantity();
    String getProductId();
    String getOptionId();
}
