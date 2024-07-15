package org.senju.eshopeule.model.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    COMPLETED("Completed"),
    PROCESSING("Processing"),
    SHIPPING("Shipping"),
    CANCELLED("Cancelled");

    private final String name;
}
