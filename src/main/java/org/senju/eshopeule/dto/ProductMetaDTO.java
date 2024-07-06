package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;
import java.util.List;
import java.util.Map;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class ProductMetaDTO implements BaseDTO {
    @Serial
    private static final long serialVersionUID = -8120144456443289737L;

    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
    private String id;

    @NotBlank(message = "Product's ID is required")
    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
    @JsonProperty(value = "product_id")
    private String productId;

    @JsonProperty(value = "meta_description")
    private String metaDescription;

    private Map<String, Object> attributes;

    private List<String> tags;
}
