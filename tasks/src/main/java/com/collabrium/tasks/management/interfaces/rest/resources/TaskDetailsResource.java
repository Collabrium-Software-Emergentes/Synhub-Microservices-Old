package com.collabrium.tasks.management.interfaces.rest.resources;

public record TaskDetailsResource(
    Long id,
    String title,
    String description,
    String dueDate,
    String createdAt,
    String updatedAt,
    String status,
    Integer timesRearranged,
    Long timePassed,
    Long memberId,
    Long groupId
) {
}