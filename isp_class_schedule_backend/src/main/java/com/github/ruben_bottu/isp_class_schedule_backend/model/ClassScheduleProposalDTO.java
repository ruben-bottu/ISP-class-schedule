package com.github.ruben_bottu.isp_class_schedule_backend.model;

import java.util.List;

public record ClassScheduleProposalDTO(
        int overlapCount,
        List<CourseAndClassGroupDTO> combination
) {}
