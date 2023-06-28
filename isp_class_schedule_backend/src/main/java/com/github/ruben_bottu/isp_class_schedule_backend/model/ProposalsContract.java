package com.github.ruben_bottu.isp_class_schedule_backend.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProposalsContract(@Size(max = MAX_COURSE_IDS_SIZE) @NoDuplicates List<@NotNull Long> courseIds, @PositiveOrZero int solutionCount) {
    public static final int DEFAULT_SOLUTION_COUNT = 10;
    public static final int MAX_SOLUTION_COUNT = 20;
    public static final int MAX_COURSE_IDS_SIZE = 30;

    public ProposalsContract(List<Long> courseIds) {
       this(courseIds, DEFAULT_SOLUTION_COUNT);
    }

    public static int toValidSolutionCount(int solutionCount) {
        return Math.min(solutionCount, MAX_SOLUTION_COUNT);
    }
}
