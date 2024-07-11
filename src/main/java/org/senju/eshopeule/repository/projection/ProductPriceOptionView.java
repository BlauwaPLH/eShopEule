package org.senju.eshopeule.repository.projection;

public interface ProductPriceOptionView {
    String getProductId();
    Double getPrice();
    Double getDiscount();
    String getOptionId();
}
