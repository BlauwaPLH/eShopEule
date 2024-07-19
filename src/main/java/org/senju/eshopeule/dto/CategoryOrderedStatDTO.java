package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryOrderedStatDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -2310721509344600038L;

    @JsonProperty(value = "category_id")
    private String categoryId;

    @JsonProperty(value = "category_name")
    private String categoryName;

    @JsonProperty(value = "count_products")
    private Long countProducts;

    @JsonProperty(value = "count_ordered_quantity")
    private Long countOrderedQuantity;

    private Double revenue;
}
