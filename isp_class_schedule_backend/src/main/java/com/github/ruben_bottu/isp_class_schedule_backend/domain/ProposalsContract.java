package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProposalsContract(@Size(max = MAX_COURSE_IDS_SIZE) @NoDuplicates List<@NotNull Long> courseIds,
                                @PositiveOrZero Integer solutionCount, ClassScheduleProperties properties) {
    public static final int MAX_COURSE_IDS_SIZE = 30;

    public ProposalsContract {
        solutionCount = (solutionCount == null)
                ? properties.defaultSolutionCount()
                : Math.min(solutionCount, properties.maxSolutionCount());
    }
}
