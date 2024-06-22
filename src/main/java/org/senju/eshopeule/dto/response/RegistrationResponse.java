package org.senju.eshopeule.dto.response;

import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class RegistrationResponse implements BaseResponse {

    @Serial
    private static final long serialVersionUID = 4338969675148873368L;

    private String message;
}
