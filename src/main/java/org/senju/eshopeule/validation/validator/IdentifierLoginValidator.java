package org.senju.eshopeule.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.senju.eshopeule.validation.constraints.IdentifierLoginConstraint;

import static org.senju.eshopeule.constant.pattern.RegexPattern.*;

public class IdentifierLoginValidator implements ConstraintValidator<IdentifierLoginConstraint, String> {

    @Override
    public boolean isValid(String identifier, ConstraintValidatorContext context) {
        return identifier.matches(USERNAME_PATTERN) || identifier.matches(EMAIL_PATTERN);
    }
}
