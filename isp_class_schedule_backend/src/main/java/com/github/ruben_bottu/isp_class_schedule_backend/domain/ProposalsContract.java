package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.external.AnnotationUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * ProposalsContract provides a framework-independent way of validating proposal requests
 * <p>
 * This class uses reflection in order to programmatically fill in a value for max in @Size(max = <value>)
 *
 * @param courseIds
 * @param solutionCount
 * @param properties
 */
public record ProposalsContract(@Size(max = 0) @NoDuplicates List<@NotNull Long> courseIds,
                                @PositiveOrZero Integer solutionCount, ClassScheduleProperties properties) {
    private static final String REFLECTION_FIELD_NAME = "courseIds";
    private static final int SIZE_ANNOTATION_POSITION = 0;
    private static final String INPUT_RESOURCE = "application.yml";

    static {
        AnnotationUtil.changeAnnotationValue(getCourseIdsSizeAnnotation(), "max", getMaxCourseIdsSize());
    }

    public ProposalsContract {
        solutionCount = (solutionCount == null)
                ? properties.defaultSolutionCount()
                : Math.min(solutionCount, properties.maxSolutionCount());
    }

    private static Annotation getCourseIdsSizeAnnotation() {
        try {
            return ProposalsContract.class.getDeclaredField(REFLECTION_FIELD_NAME).getDeclaredAnnotations()[SIZE_ANNOTATION_POSITION];
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getMaxCourseIdsSize() {
        var mapper = new ObjectMapper(new YAMLFactory());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (InputStream is = classloader.getResourceAsStream(INPUT_RESOURCE)) {
            return mapper.readValue(is, ClassSchedulePropertiesWrapper.class)
                    .classSchedule
                    .maxCourseIdsSize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ClassSchedulePropertiesWrapper exists specifically for mapping from YAML properties to object
     * @param classSchedule
     */
    private record ClassSchedulePropertiesWrapper(ClassScheduleProperties classSchedule) {
    }
}
