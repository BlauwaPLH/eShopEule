package org.senju.eshopeule.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.senju.eshopeule.validation.constraints.RatingStarConstraint;

public class RatingStarValidator implements ConstraintValidator<RatingStarConstraint, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value >= 0 && value <= 5;
    }

}
