package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;
import java.util.Map;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class ProductOptionDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -8695399292401270587L;

    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
    private String id;

    private String name;

    @JsonProperty(value = "product_id")
    private String productId;

    private Map<String, ProductAttributeValueDTO> attributes;
}
