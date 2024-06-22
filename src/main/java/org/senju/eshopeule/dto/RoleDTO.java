package org.senju.eshopeule.dto;

import lombok.*;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class RoleDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -2871661541103513672L;

    private String id;
    private String name;
    private String description;
    private List<PermissionDTO> permissions;
}
