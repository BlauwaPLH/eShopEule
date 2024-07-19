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
public final class BrandStatDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = 2700358743114303599L;

    @JsonProperty(value = "brand_id")
    private String brandId;

    @JsonProperty(value = "brand_name")
    private String brandName;

    @JsonProperty(value = "count_products")
    private long countProducts;

    @JsonProperty(value = "total_brand_order")
    private long totalBrandOrder;
}
