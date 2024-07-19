package org.senju.eshopeule.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;

import static org.senju.eshopeule.constant.pattern.RegexPattern.USERNAME_PATTERN;
import static org.senju.eshopeule.constant.pattern.RegexPattern.VERIFICATION_CODE_PATTERN;

@Getter
public final class VerifyRequest implements BaseRequest {

    @Serial
    private static final long serialVersionUID = -4722583543674372538L;

    @JsonProperty("username")
    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = USERNAME_PATTERN, message = "Username is invalid")
    private String username;


    @JsonProperty("verify_code")
    @NotBlank(message = "Verification code is mandatory")
    @Pattern(regexp = VERIFICATION_CODE_PATTERN, message = "Verification code is invalid")
    private String verifyCode;
}

