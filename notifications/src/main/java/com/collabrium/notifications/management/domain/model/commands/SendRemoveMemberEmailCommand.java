package com.collabrium.notifications.management.domain.model.commands;

public record SendRemoveMemberEmailCommand(
    String memberEmail,
    String groupName,
    String groupImageUrl,
    String groupCode,
    String leaderEmail
) {
}