package org.senju.eshopeule.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.senju.eshopeule.validation.validator.DiscountValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DiscountValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DiscountConstraint {

    String message() default "Discount must be between 0% and 100%";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
