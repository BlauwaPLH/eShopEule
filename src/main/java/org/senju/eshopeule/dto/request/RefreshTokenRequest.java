package org.senju.eshopeule.dto.request;

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

    private String refreshToken;

}
