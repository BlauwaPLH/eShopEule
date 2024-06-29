package org.senju.eshopeule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;

import static org.senju.eshopeule.constant.pattern.RegexPattern.EMAIL_PATTERN;
import static org.senju.eshopeule.constant.pattern.RegexPattern.USERNAME_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class ResendVerifyCodeRequest implements BaseRequest{

    @Serial
    private static final long serialVersionUID = -3068109530764649647L;

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = EMAIL_PATTERN, message = "Email is invalid")
    private String email;

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = USERNAME_PATTERN, message = "Username is invalid")
    private String username;
}
