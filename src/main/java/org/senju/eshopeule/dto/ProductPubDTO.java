package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.senju.eshopeule.validation.constraints.ValidPriceConstraint;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProductPubDTO implements ProductDTO {

    @Serial
    private static final long serialVersionUID = -3039869086177871211L;

    private String id;

    private String name;

    private String slug;

    @ValidPriceConstraint
    private Double price;

    private Double discount;

    @JsonProperty(value = "has_options")
    private Boolean hasOptions;

    @JsonProperty(value = "is_allowed_to_order")
    private Boolean isAllowedToOrder;

    private String brand;

    private List<String> categories;

    @JsonProperty(value = "image_urls")
    private List<String> imageUrls;

    private List<ProductOptionDTO> options;

    @JsonProperty(value = "product_meta")
    private ProductMetaDTO productMeta;
}
