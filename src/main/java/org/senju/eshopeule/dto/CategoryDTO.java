package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
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

    @JsonProperty(value = "create_on")
    private ZonedDateTime createdOn;

    @JsonProperty(value = "created_by")
    private String createdBy;

    @JsonProperty(value = "last_modified_on")
    private ZonedDateTime lastModifiedOn;

    @JsonProperty(value = "last_modified_by")
    private String lastModifiedBy;
}
