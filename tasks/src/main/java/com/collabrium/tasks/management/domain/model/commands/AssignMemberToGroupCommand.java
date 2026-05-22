package com.collabrium.tasks.management.domain.model.commands;

public record AssignMemberToGroupCommand(
    Long memberId,
    Long groupId
) {
}