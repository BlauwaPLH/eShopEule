package org.senju.eshopeule.dto.response;

import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class LoginResponse implements BaseResponse {

    @Serial
    private static final long serialVersionUID = 7650455188171148671L;

    private String message;
    private String accessToken;
    private String refreshToken;
}
