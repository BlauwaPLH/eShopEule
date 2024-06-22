package org.senju.eshopeule.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.senju.eshopeule.dto.request.RegistrationRequest;
import org.senju.eshopeule.validation.constraints.EmailOrPhoneConstraint;

public class EmailOrPhoneValidator implements ConstraintValidator<EmailOrPhoneConstraint, RegistrationRequest> {

    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext context) {
        return !((request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) &&
                (request.getEmail() == null || request.getEmail().trim().isEmpty()));
    }
}
