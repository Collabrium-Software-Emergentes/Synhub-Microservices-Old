package com.collabrium.notifications.management.domain.model.commands;

public record SendRequestCreatedEmailCommand(
    String leaderEmail,
    String memberUsername,
    String memberName,
    String memberSurname,
    String taskTitle,
    String requestDescription,
    String requestType,
    String imageUrl
) {
}
