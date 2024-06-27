package org.senju.eshopeule.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class RefreshTokenRequest implements BaseRequest {

    @Serial
    private static final long serialVersionUID = 653877131847090922L;

    @JsonProperty(value = "refresh_token")
    @NotBlank(message = "Refresh token is not empty")
    private String refreshToken;

}
