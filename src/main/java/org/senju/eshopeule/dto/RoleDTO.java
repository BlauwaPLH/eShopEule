package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;
import java.util.List;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class RoleDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -2871661541103513672L;

    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
    private String id;
    private String name;
    private String description;
    private List<PermissionDTO> permissions;
}
