package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProdOrderStatusStatDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = 1837625279026510422L;

    @JsonProperty(value = "order_status")
    private String orderStatus;

    @JsonProperty(value = "total_value")
    private double totalValue;

    @JsonProperty(value = "total_quantity")
    private long totalQuantity;
}
