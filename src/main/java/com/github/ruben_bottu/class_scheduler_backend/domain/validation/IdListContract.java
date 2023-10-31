package com.github.ruben_bottu.class_scheduler_backend.domain.validation;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record IdListContract(@MaxSize @NoDuplicates List<@NotNull Long> ids) {
}
