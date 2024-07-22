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
public class OrderedProdOptionStatDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -1075030213110211241L;

    @JsonProperty(value = "product_id")
    private String productId;

    @JsonProperty(value = "product_name")
    private String productName;

    @JsonProperty(value = "option_id")
    private String optionId;

    @JsonProperty(value = "option_name")
    private String optionName;

    @JsonProperty(value = "total_value")
    private Double totalValue;
}
