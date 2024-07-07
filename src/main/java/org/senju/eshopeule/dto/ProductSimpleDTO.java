package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProductSimpleDTO implements ProductDTO {

    @Serial
    private static final long serialVersionUID = -2475496787277491678L;

    private String id;

    private String name;

    private String slug;

    @JsonProperty("image_url")
    private String imageUrl;

    private Double price;

    private Double discount;

}
