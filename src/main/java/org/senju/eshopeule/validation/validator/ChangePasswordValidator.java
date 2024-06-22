package org.senju.eshopeule.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.senju.eshopeule.dto.request.ChangePasswordRequest;
import org.senju.eshopeule.validation.constraints.ChangePasswordConstraint;

public class ChangePasswordValidator implements ConstraintValidator<ChangePasswordConstraint, ChangePasswordRequest> {

    @Override
    public boolean isValid(ChangePasswordRequest request, ConstraintValidatorContext context) {
        return !request.getOldPassword().equals(request.getNewPassword());
    }
}
