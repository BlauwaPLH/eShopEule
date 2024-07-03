package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class BrandDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -1460781530819856488L;

    private String id;

    private String name;

    private String slug;

    @JsonProperty(value = "create_on")
    private ZonedDateTime createdOn;

    @JsonProperty(value = "created_by")
    private String createdBy;

    @JsonProperty(value = "last_modified_on")
    private ZonedDateTime lastModifiedOn;

    @JsonProperty(value = "last_modified_by")
    private String lastModifiedBy;
}
