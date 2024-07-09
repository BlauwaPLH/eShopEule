package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.repository.query.Param;

import java.io.Serial;
import java.time.ZonedDateTime;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class BrandDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -1460781530819856488L;

    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
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
