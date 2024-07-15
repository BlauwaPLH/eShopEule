package org.senju.eshopeule.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.senju.eshopeule.validation.validator.RatingStarValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RatingStarValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RatingStarConstraint {

    String message() default "Rating star value must be between 0 and 5";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
