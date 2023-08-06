package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.course_group.CourseGroup;

import java.util.List;

public record ClassScheduleProposal(
        int overlapCount,
        List<CourseGroup> combination
) {
}
