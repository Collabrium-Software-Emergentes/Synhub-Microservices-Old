package com.collabrium.notifications.management.domain.model.commands;

public record SendGroupCreatedEmailCommand(
    String groupName,
    String groupDescription,
    String imgUrl,
    String code,
    String leaderEmail
) {
}