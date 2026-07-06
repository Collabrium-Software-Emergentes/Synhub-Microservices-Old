package com.collabrium.notifications.management.domain.model.commands;

public record SendInvitationAcceptedEmailCommand(
    String groupName,
    String groupDescription,
    String groupImgUrl,
    String groupCode,
    String memberEmail
) {
}