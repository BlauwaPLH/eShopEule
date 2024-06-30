package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;

import static org.senju.eshopeule.constant.pattern.RegexPattern.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class StaffDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -4722816414815982021L;

    private String id;

    @Pattern(regexp = USERNAME_PATTERN, message = "Username is invalid")
    private String username;

    @Pattern(regexp = EMAIL_PATTERN, message = "Email is invalid")
    private String email;

    @Pattern(regexp = PASSWORD_PATTERN, message = "Password is invalid")
    private String password;

    @JsonProperty(value = "phone_number")
    @Pattern(regexp = PHONE_PATTERN, message = "Phone number is invalid")
    private String phoneNumber;

    private boolean enabled;

    private RoleDTO role;
}
