package com.collabrium.notifications.management.domain.model.commands;

public record SendInvitationAcceptedEmailCommand(
    String groupName,
    String groupImgUrl,
    String groupCode,
    String memberEmail
) {
}