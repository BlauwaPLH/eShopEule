package org.senju.eshopeule.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.senju.eshopeule.validation.constraints.ValidUsernameLoginConstraint;

import static org.senju.eshopeule.constant.pattern.RegexPattern.*;

public class ValidUsernameLoginValidator implements ConstraintValidator<ValidUsernameLoginConstraint, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username.matches(USERNAME_PATTERN) || username.matches(EMAIL_PATTERN) || username.matches(PHONE_PATTERN);
    }
}
