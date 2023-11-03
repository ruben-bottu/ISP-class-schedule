package com.github.ruben_bottu.class_scheduler_backend.controller.dto;

import com.github.ruben_bottu.class_scheduler_backend.domain.Group;

public record GroupDTO(
        Long id,
        String name
) {

    public GroupDTO(Group group) {
        this(group.id(), group.name());
    }
}
