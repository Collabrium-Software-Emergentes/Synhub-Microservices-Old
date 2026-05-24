package com.collabrium.tasks.management.domain.model.commands;

public record UpdateTaskStatusCommand(
    Long taskId,
    String status,
    Long userId
) {
}