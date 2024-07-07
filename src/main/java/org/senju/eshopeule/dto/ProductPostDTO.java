package org.senju.eshopeule.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.senju.eshopeule.validation.constraints.ValidDiscountConstraint;
import org.senju.eshopeule.validation.constraints.ValidPriceConstraint;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class ProductPostDTO implements ProductDTO {

    @Serial
    private static final long serialVersionUID = 1472268047681916737L;

    @NotBlank(message = "Product's name is required")
    private String name;

    @NotBlank(message = "Product's slug is required")
    private String slug;

    private String sku;

    private String gtin;

    @NotNull(message = "Product's price is required")
    @ValidPriceConstraint
    private Double price;

    @ValidDiscountConstraint
    private Double discount;

    @NotNull(message = "Product's quantity is required")
    @Min(value = 0, message = "Product's quantity must be greater than 0")
    private Long quantity;

    @JsonProperty(value = "has_options")
    @NotNull(message = "Field 'Has Options' is required")
    private Boolean hasOptions;

    @JsonProperty(value = "is_published")
    @NotNull(message = "Field 'Is Published' is required")
    private Boolean isPublished;

    @JsonProperty(value = "is_allowed_to_order")
    @NotNull(message = "Field 'Is Allowed To Order' is required")
    private Boolean isAllowedToOrder;

    private String description;

    @JsonProperty(value = "brand_id")
    @NotBlank(message = "Field 'Brand' is required")
    private String brandId;

    @JsonProperty(value = "category_ids")
    @NotNull(message = "Field 'Categories' is required")
    private List<String> categoryIds;

    private List<ProductOptionDTO> options;

    private ProductMetaDTO productMeta;
}
