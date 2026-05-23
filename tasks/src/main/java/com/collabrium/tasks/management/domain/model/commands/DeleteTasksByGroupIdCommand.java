package com.collabrium.tasks.management.domain.model.commands;

public record DeleteTasksByGroupIdCommand(
    Long groupId
) {
}