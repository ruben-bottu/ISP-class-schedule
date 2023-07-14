package com.github.ruben_bottu.isp_class_schedule_backend.domain.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassScheduleProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class MaxSizeFromConstraintValidator implements ConstraintValidator<MaxSizeFrom, Collection<?>> {
    private String resource;

    @Override
    public void initialize(MaxSizeFrom maxSizeValue) {
        this.resource = maxSizeValue.resource();
    }

    private int getMaxCourseIdsSize() {
        var mapper = new ObjectMapper(new YAMLFactory());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (InputStream is = classloader.getResourceAsStream(resource)) {
            return mapper.readValue(is, ClassSchedulePropertiesWrapper.class)
                    .classSchedule
                    .maxCourseIdsSize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isValid(Collection<?> collection, ConstraintValidatorContext context) {
        return collection.size() <= getMaxCourseIdsSize();
    }

    /**
     * This class exists for mapping from YAML properties to object
     *
     * @param classSchedule
     */
    private record ClassSchedulePropertiesWrapper(ClassScheduleProperties classSchedule) {
    }
}
