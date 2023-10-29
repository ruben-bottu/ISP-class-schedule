package com.github.ruben_bottu.isp_class_schedule_backend.controller.dto;

import java.util.List;

public record ProposalDTO(
        int overlapCount,
        List<CourseGroupDTO> combination
) {
}
