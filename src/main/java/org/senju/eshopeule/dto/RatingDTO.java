package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.senju.eshopeule.validation.constraints.RatingStarConstraint;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class RatingDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = 2867749875196007252L;

    private String id;

    @NotBlank(message = "Product ID must be required")
    @JsonProperty(value = "product_id")
    private String productId;

    @JsonProperty(value = "customer_name")
    private String customerName;

    @JsonProperty(value = "rating_star")
    @RatingStarConstraint
    private Integer ratingStar;

    private String content;
}
