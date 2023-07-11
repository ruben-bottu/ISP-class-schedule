package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = NoDuplicatesConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicates {
    String message() default "{beancheck.duplicates}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
