package com.github.ruben_bottu.class_scheduler_backend.domain.validation;

import com.github.ruben_bottu.class_scheduler_backend.domain.ClassScheduleProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

/**
 * ProposalsContract provides a framework-independent way of validating proposal requests
 *
 * @param courseIds
 * @param solutionCount
 * @param properties
 */
public record ProposalsContract(@MaxSize @NoDuplicates List<@NotNull Long> courseIds,
                                @PositiveOrZero int solutionCount, ClassScheduleProperties properties) {

    public ProposalsContract {
        // -1 is interpreted as the "empty" value here, so it receives the default
        solutionCount = (solutionCount == -1)
                ? properties.defaultSolutionCount()
                : Math.min(solutionCount, properties.maxSolutionCount());
    }
}
