package com.github.ruben_bottu.class_scheduler_backend.domain;

public record ClassScheduleProperties(int defaultSolutionCount,
                                      int maxSolutionCount,
                                      int maxCourseIdsSize) {
}
