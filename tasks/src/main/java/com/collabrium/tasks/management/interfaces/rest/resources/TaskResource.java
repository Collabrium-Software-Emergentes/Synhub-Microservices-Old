package com.collabrium.tasks.management.interfaces.rest.resources;

public record TaskResource(
    Long id,
    String title,
    String description,
    String dueDate,
    String createdAt,
    String updatedAt,
    String status,
    Integer timesRearranged,
    Long timePassed,
    TaskMemberResource member,
    Long groupId,
    String imageUrl
) {
}