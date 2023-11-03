package com.github.ruben_bottu.class_scheduler_backend.controller.dto;

import com.github.ruben_bottu.class_scheduler_backend.domain.course_group.CourseGroup;

import java.util.List;

public record ProposalDTO(
        String averageWeeklyOverlapCount,
        List<CourseGroupDTO> combination
) {

    public static List<CourseGroupDTO> mapToDto(List<CourseGroup> courseGroups) {
        return courseGroups.stream().map(CourseGroupDTO::new).toList();
    }
}
