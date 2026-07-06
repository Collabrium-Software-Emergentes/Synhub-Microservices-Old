package com.collabrium.tasks.management.interfaces.rest.resources;

import java.time.OffsetDateTime;

public record UpdateTaskResource(
        String title,
        String description,
        OffsetDateTime dueDate,
        Long memberId
) {
}