package org.senju.eshopeule.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.senju.eshopeule.validation.constraints.ValidDiscountConstraint;

public class ValidDiscountValidator implements ConstraintValidator<ValidDiscountConstraint, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value >= 0.0 && value <= 100.0;
    }
}
