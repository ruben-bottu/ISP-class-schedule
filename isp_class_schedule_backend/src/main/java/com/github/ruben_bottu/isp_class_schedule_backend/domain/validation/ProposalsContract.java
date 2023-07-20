package com.github.ruben_bottu.isp_class_schedule_backend.domain.validation;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassScheduleProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

/**
 * ProposalsContract provides a framework-independent way of validating proposal requests
 * <p>
 * Beware that this class loads values from application.yml through a custom annotation
 *
 * @param courseIds
 * @param solutionCount
 * @param properties
 */
public record ProposalsContract(@MaxSizeFrom/*(resource = "application.yml")*/ @NoDuplicates List<@NotNull Long> courseIds,
                                @PositiveOrZero Integer solutionCount, ClassScheduleProperties properties) {

    public ProposalsContract {
        solutionCount = (solutionCount == null)
                ? properties.defaultSolutionCount()
                : Math.min(solutionCount, properties.maxSolutionCount());
    }
}
