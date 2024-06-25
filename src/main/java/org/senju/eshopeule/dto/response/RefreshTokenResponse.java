package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class RefreshTokenResponse implements BaseResponse {

    @Serial
    private static final long serialVersionUID = 2120613545888493465L;

    private String message;

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;
}
