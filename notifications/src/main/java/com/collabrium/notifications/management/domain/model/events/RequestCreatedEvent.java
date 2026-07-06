package com.collabrium.notifications.management.domain.model.events;

public record RequestCreatedEvent(
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
