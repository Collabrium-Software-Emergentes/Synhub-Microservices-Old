package com.collabrium.tasks.management.interfaces.rest.resources;

import java.time.OffsetDateTime;

public record CreateTaskResource(
        String title,
        String description,
        OffsetDateTime dueDate
) {
}