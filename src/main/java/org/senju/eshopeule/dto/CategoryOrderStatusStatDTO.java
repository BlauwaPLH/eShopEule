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
public class CategoryOrderStatusStatDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = 4747061419948693773L;

    @JsonProperty(value = "order_status")
    private String orderStatus;

    @JsonProperty(value = "total_order_value")
    private Double totalOrderValue;

    @JsonProperty(value = "quantity")
    private Long totalQuantity;
}
