package com.collabrium.notifications.management.domain.model.commands;

public record SendGroupDeletedEmailCommand(
    String groupName,
    String groupDescription,
    String groupImageUrl,
    String groupCode,
    String leaderEmail,
    String memberEmail
) {
}