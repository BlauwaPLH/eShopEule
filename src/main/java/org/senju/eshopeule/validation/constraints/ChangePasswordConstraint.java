package org.senju.eshopeule.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.senju.eshopeule.validation.validator.ChangePasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = ChangePasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangePasswordConstraint {
    String message() default "New password must be different from the old password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
