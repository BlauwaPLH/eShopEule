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
public class OrderedProductStatDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = 8292156739833756501L;

    @JsonProperty(value = "product_id")
    private String productId;

    @JsonProperty(value = "product_name")
    private String productName;

    @JsonProperty(value = "total_revenue")
    private Double totalRevenue;

    @JsonProperty(value = "total_ordered_quantity")
    private Long totalOrderedQuantity;
}
