package com.collabrium.notifications.management.domain.model.events;

public record GroupCreatedEvent(
    String groupName,
    String groupDescription,
    String imgUrl,
    String code,
    String leaderEmail
) {
}