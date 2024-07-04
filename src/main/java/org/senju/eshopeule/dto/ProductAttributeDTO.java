package org.senju.eshopeule.dto;


import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class ProductAttributeDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -5863841881598415809L;

    private String id;
    private String name;
}
