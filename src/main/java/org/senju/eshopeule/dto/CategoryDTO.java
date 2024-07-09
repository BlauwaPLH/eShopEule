package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.List;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class CategoryDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -5965572289040328164L;

    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
    private String id;

    private String name;

    private String slug;

    private String description;

    @JsonProperty(value = "meta_keyword")
    private String metaKeyword;

    @JsonProperty(value = "meta_description")
    private String metaDescription;

    @JsonProperty(value = "is_published")
    private boolean isPublished;

    private CategoryDTO parent;

    @JsonProperty(value = "product_attributes")
    private List<ProductAttributeDTO> productAttributes;

    @JsonProperty(value = "create_on")
    private ZonedDateTime createdOn;

    @JsonProperty(value = "created_by")
    private String createdBy;

    @JsonProperty(value = "last_modified_on")
    private ZonedDateTime lastModifiedOn;

    @JsonProperty(value = "last_modified_by")
    private String lastModifiedBy;
}
