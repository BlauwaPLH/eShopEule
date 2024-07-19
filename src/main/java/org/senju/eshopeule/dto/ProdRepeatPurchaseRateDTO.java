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
public final class ProdRepeatPurchaseRateDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = 3898620779117937794L;

    @JsonProperty(value = "product_id")
    private String productId;

    @JsonProperty(value = "count_unique_buyers")
    private Integer countUniqueBuyers;

    @JsonProperty(value = "count_frequent_buyers")
    private Integer countFrequentBuyers;

    @JsonProperty(value = "repeat_purchase_rate")
    private Double repeatPurchaseRate;
}
