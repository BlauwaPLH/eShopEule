package org.senju.eshopeule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;

import static org.senju.eshopeule.constant.pattern.RegexPattern.*;

@Getter
public final class RegistrationRequest implements BaseRequest {

    @Serial
    private static final long serialVersionUID = -7248245700460613926L;

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = USERNAME_PATTERN, message = "Username is not valid")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = EMAIL_PATTERN, message = "Email is not valid")
    private String email;

    @Pattern(regexp = PHONE_PATTERN, message = "Phone number is not valid")
    private String phoneNumber;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = PASSWORD_PATTERN, message = "Password is not valid")
    private String password;
}
