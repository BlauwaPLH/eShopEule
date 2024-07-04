package org.senju.eshopeule.dto.response;

import lombok.*;

import java.io.Serial;

@Getter
@Setter
@AllArgsConstructor
public final class SimpleResponse implements BaseResponse {
    @Serial
    private static final long serialVersionUID = -8805791124352662262L;
    private String message;
}
