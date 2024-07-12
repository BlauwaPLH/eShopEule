package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CartItemDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = 378872504899767122L;

    private String id;

    @NotNull(message = "Product's quantity is required")
    @Min(value = 0, message = "Product's quantity must be greater than 0")
    private Integer quantity;

    private ProductOptionDTO option;

    private ProductSimpleDTO product;
}
