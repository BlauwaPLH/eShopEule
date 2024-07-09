package org.senju.eshopeule.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PermissionDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -6901024370470808190L;

    @Pattern(regexp = ID_PATTERN, message = "ID is invalid")
    private String id;
    private String name;
    private String description;
}
