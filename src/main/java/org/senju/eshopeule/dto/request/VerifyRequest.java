package org.senju.eshopeule.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class VerifyRequest implements BaseRequest {

    @Serial
    private static final long serialVersionUID = -4722583543674372538L;

    @JsonProperty()
    private String identifier;

    @JsonProperty("verify_code")
    private String verifyCode;
}

