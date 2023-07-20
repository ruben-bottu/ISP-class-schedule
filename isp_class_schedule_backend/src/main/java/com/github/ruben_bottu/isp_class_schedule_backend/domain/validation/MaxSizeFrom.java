package com.github.ruben_bottu.isp_class_schedule_backend.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = MaxSizeFromConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxSizeFrom {

    String message() default "size has to be smaller than or equal to configured size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    //String resource();
}
