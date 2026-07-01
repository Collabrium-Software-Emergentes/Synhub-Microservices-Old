package com.collabrium.notifications.management.domain.model.events;

public record RemoveMemberEvent(
    String memberEmail,
    String groupName,
    String groupImageUrl,
    String groupCode,
    String leaderEmail
) {
}