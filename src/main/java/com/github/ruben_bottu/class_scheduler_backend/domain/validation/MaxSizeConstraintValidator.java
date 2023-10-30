package com.github.ruben_bottu.class_scheduler_backend.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;


/**
 * This class allows the max value of the constraint to be set at runtime.
 * Prefer using the @Size annotation from Jakarta Validation in most cases.
 */
// Only allows collections because size method is needed
public class MaxSizeConstraintValidator implements ConstraintValidator<MaxSize, Collection<?>> {

    /**
     * Should only be set once in the entire application lifetime
     */
    private static int maxSize = -1;

    private static boolean isMaxSizePresent() {
        return maxSize != -1;
    }

    public synchronized static void setMaxSize(int maxSize) {
        if (isMaxSizePresent()) throw new IllegalStateException("maxSize has already been set");
        if (maxSize < 0) throw new IllegalArgumentException("maxSize cannot be negative");
        MaxSizeConstraintValidator.maxSize = maxSize;
    }

    @Override
    public boolean isValid(Collection<?> collection, ConstraintValidatorContext context) {
        if (isMaxSizePresent()) {
            return collection.size() <= maxSize;
        }
        throw new IllegalStateException("Please set field maxSize in " + this.getClass().getSimpleName());
    }
}
