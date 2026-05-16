package com.collabrium.groups.management.domain.model.commands;

public record CreateInvitationCommand(
    Long memberId,
    Long groupId
) {
}