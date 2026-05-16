package com.collabrium.groups.management.domain.model.commands;

public record RejectInvitationCommand(
    Long leaderId,
    Long invitationId
) {
}