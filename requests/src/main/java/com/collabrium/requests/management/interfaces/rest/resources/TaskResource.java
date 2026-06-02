package com.collabrium.requests.management.interfaces.rest.resources;

public record TaskResource(
    Long id,
    String title,
    String description,
    String dueDate,
    String createdAt,
    String updatedAt,
    String status,
    TaskMemberResource member,
    Long groupId
) {
}