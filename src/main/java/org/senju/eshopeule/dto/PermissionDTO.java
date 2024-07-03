package org.senju.eshopeule.dto;

import lombok.*;

import java.io.Serial;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PermissionDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -6901024370470808190L;

    private String id;
    private String name;
    private String description;
}
