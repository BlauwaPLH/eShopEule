package org.senju.eshopeule.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.senju.eshopeule.validation.constraints.ChangePasswordConstraint;

import java.io.Serial;

import static org.senju.eshopeule.constant.pattern.RegexPattern.PASSWORD_PATTERN;

@Getter
@ChangePasswordConstraint
public final class ChangePasswordRequest implements BaseRequest {

    @Serial
    private static final long serialVersionUID = 7455659579883465251L;

    @JsonProperty(value = "new_password")
    @NotBlank(message = "Old password is mandatory")
    @Pattern(regexp = PASSWORD_PATTERN,
            message = "Password must be 8-16 characters long and include a mix of upper/lower case letters, numbers, and special characters")
    private String newPassword;

    @JsonProperty(value = "old_password")
    @NotBlank(message = "New password is mandatory")
    @Pattern(regexp = PASSWORD_PATTERN,
            message = "Password must be 8-16 characters long and include a mix of upper/lower case letters, numbers, and special characters")
    private String oldPassword;
}
