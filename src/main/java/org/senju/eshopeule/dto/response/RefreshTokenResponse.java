package org.senju.eshopeule.dto.response;

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
    private String accessToken;
    private String refreshToken;
}
