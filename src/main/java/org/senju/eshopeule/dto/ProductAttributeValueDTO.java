package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProductAttributeValueDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -9218534996942955188L;

    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
    private String id;
    private String value;

    public ProductAttributeValueDTO(String value) {
        this.value = value;
    }
}
