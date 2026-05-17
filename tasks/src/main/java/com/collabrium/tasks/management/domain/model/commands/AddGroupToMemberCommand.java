package com.collabrium.tasks.management.domain.model.commands;

public record AddGroupToMemberCommand(
    Long groupId,
    Long memberId
) {
}