package com.collabrium.groups.management.domain.model.commands;

public record CancelInvitationCommand(
    Long memberId,
    Long invitationId
) {
}