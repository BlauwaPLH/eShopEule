package org.senju.eshopeule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.senju.eshopeule.validation.constraints.ValidIdentifierLoginConstraint;

import java.io.Serial;

import static org.senju.eshopeule.constant.pattern.RegexPattern.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class LoginRequest implements BaseRequest {

    @Serial
    private static final long serialVersionUID = 1985682314215597148L;

    @ValidIdentifierLoginConstraint
    private String identifier;

    @NotBlank
    @Pattern(regexp = PASSWORD_PATTERN,
            message = "Password must be 8-16 characters long and include a mix of upper/lower case letters, numbers, and special characters")
    private String password;
}
