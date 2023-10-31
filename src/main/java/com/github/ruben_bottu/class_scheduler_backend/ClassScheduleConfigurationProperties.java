package com.github.ruben_bottu.class_scheduler_backend;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "class-schedule")
@ConfigurationPropertiesScan
@Validated
public record ClassScheduleConfigurationProperties(@NotNull Integer defaultSolutionCount,
                                                   @NotNull Integer maxSolutionCount,
                                                   @NotNull Integer maxIdListSize) {
}
