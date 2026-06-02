package com.collabrium.metrics.shared.infrastructure.clients.tasks.resources;

public record TaskOnlyResource(
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