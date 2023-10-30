package com.github.ruben_bottu.class_scheduler_backend.controller.dto;

import java.util.List;

public record ProposalDTO(
        int overlapCount,
        List<CourseGroupDTO> combination
) {
}
