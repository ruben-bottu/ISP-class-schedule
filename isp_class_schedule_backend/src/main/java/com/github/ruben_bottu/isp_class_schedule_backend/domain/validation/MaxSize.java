package com.github.ruben_bottu.isp_class_schedule_backend.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation allows the max value of the constraint to be set at runtime.
 * You should prefer using the @Size annotation from Jakarta Validation in most cases.
 */
@Constraint(validatedBy = MaxSizeConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxSize {

    String message() default "size has to be smaller than or equal to configured size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
