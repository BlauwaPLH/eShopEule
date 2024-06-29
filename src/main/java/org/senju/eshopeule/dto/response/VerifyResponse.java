package org.senju.eshopeule.dto.response;

import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class VerifyResponse implements BaseResponse {

    @Serial
    private static final long serialVersionUID = -5894601662654026000L;

    private String message;
}
