package com.collabrium.groups.management.domain.model.commands;

public record AcceptInvitationCommand(
    Long leaderId,
    Long invitationId
) {
}