package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.senju.eshopeule.validation.constraints.ValidDiscountConstraint;
import org.senju.eshopeule.validation.constraints.ValidPriceConstraint;

import java.io.Serial;
import java.util.List;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class ProductPutDTO implements ProductDTO {

    @Serial
    private static final long serialVersionUID = 2875023085972024095L;

    @NotBlank(message = "Product ID is required")
    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
    private String id;

    private String name;

    private String slug;

    private String sku;

    private String gtin;

    @ValidPriceConstraint
    private Double price;

    @ValidDiscountConstraint
    private Double discount;

    @Min(value = 0, message = "Product's quantity must be greater than 0")
    private Long quantity;

    @JsonProperty(value = "has_options")
    private Boolean hasOptions;

    @JsonProperty(value = "is_published")
    private Boolean isPublished;

    @JsonProperty(value = "is_allowed_to_order")
    private Boolean isAllowedToOrder;

    private String description;

    @JsonProperty(value = "brand_id")
    private String brandId;

    @JsonProperty(value = "category_ids")
    private List<String> categoryIds;
}
