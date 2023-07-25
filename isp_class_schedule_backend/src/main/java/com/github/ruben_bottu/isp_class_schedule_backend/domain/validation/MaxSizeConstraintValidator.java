package com.github.ruben_bottu.isp_class_schedule_backend.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;


/**
 * This class allows the max value of the constraint to be set at runtime.
 * You should prefer using the @Size annotation from Jakarta Validation in most cases.
 */
// Only allows collections because size method is needed
public class MaxSizeConstraintValidator implements ConstraintValidator<MaxSize, Collection<?>> {

    /**
     * Should only be set one time in the entire application lifetime
     */
    public static int maxSize = -1;

    @Override
    public boolean isValid(Collection<?> collection, ConstraintValidatorContext context) {
        if (maxSize == -1) {
            throw new IllegalStateException("Please set field maxSize in " + this.getClass().getSimpleName());
        }
        return collection.size() <= maxSize;
    }
}
