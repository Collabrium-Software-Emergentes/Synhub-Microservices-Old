package com.collabrium.tasks.management.domain.model.commands;

public record DeleteMembersByGroupIdCommand(
    Long groupId
) {
}