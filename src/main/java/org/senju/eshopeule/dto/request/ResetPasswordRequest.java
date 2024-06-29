package org.senju.eshopeule.dto.request;

import lombok.*;
import org.senju.eshopeule.validation.constraints.ValidIdentifierLoginConstraint;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest implements BaseRequest {

    @ValidIdentifierLoginConstraint
    private String identifier;
}
