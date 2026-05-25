package com.collabrium.tasks.management.domain.model.queries;

public record GetAllTaskDetailsByStatusQuery(
    String taskStatus
) {
}