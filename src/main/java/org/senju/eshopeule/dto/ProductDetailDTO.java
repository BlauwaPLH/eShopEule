package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProductDetailDTO implements ProductDTO {

    @Serial
    private static final long serialVersionUID = -4006719376146888572L;

    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
    private String id;

    private String name;

    private String sku;

    private String gtin;

    private String slug;

    private Double price;

    private Double discount;

    private Long quantity;

    @JsonProperty(value = "has_options")
    private boolean hasOptions;

    @JsonProperty(value = "is_published")
    private boolean isPublished;

    @JsonProperty(value = "is_allowed_to_order")
    private boolean isAllowedToOrder;

    private String description;

    private BrandDTO brand;

    private List<CategoryDTO> categories;

    @JsonProperty(value = "image_urls")
    private List<String> imageUrls;

    private List<ProductOptionDTO> options;

    @JsonProperty(value = "product_meta")
    private ProductMetaDTO productMeta;

    @JsonProperty(value = "create_on")
    private LocalDateTime createdOn;

    @JsonProperty(value = "created_by")
    private String createdBy;

    @JsonProperty(value = "last_modified_on")
    private LocalDateTime lastModifiedOn;

    @JsonProperty(value = "last_modified_by")
    private String lastModifiedBy;
}
