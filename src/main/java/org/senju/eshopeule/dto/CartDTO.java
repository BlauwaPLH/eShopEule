package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CartDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = 1677707147250484646L;

    private String id;
    private String status;
    private Double total;
    private List<CartItemDTO> items;

    @JsonProperty(value = "customer_id")
    private String customerId;
}
