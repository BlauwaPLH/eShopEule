package org.senju.eshopeule.dto.request;

import lombok.*;
import org.senju.eshopeule.validation.constraints.IdentifierLoginConstraint;

@Getter
public class ResetPasswordRequest implements BaseRequest {

    @IdentifierLoginConstraint
    private String identifier;
}
