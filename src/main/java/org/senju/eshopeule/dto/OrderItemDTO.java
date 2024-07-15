package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class OrderItemDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -750676465152314150L;

    private Integer quantity;
    private Double total;
    private ProductOptionDTO option;
    private ProductSimpleDTO product;
}
