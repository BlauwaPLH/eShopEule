package org.senju.eshopeule.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.senju.eshopeule.validation.validator.ValidUsernameLoginValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidUsernameLoginValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsernameLoginConstraint {
    String message() default "Must be a valid username, email, or phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
