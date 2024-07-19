package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class OrderStatusStatDTO implements BaseDTO {
    @Serial
    private static final long serialVersionUID = 5328878550112494077L;

    @JsonProperty(value = "order_status")
    private String orderStatus;

    @JsonProperty(value = "count_orders")
    private long countOrders;

    @JsonProperty(value = "total_value")
    private double totalValue;
}
