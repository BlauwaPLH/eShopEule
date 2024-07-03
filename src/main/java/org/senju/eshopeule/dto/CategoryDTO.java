package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class CategoryDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -5965572289040328164L;

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

    private List<CategoryDTO> children;

    @JsonProperty(value = "create_on")
    private ZonedDateTime createdOn;

    @JsonProperty(value = "created_by")
    private String createdBy;

    @JsonProperty(value = "last_modified_on")
    private ZonedDateTime lastModifiedOn;

    @JsonProperty(value = "last_modified_by")
    private String lastModifiedBy;
}
