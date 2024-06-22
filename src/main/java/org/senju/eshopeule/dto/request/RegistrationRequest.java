package org.senju.eshopeule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.senju.eshopeule.validation.constraints.EmailOrPhoneConstraint;

import java.io.Serial;

import static org.senju.eshopeule.constant.pattern.RegexPattern.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EmailOrPhoneConstraint
public final class RegistrationRequest implements BaseRequest {

    @Serial
    private static final long serialVersionUID = -7248245700460613926L;

    @Pattern(regexp = USERNAME_PATTERN)
    private String username;

    @Pattern(regexp = EMAIL_PATTERN)
    private String email;

    @Pattern(regexp = PHONE_PATTERN)
    private String phoneNumber;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;
}
