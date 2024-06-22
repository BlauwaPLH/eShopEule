package org.senju.eshopeule.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.senju.eshopeule.validation.validator.EmailOrPhoneValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailOrPhoneValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrPhoneConstraint {
    String message() default "At least one of email or phone number must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
