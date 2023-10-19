package com.github.ruben_bottu.isp_class_schedule_backend.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NoDuplicatesConstraintValidator.class)
public @interface NoDuplicates {
    String message() default "{beancheck.duplicates}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
