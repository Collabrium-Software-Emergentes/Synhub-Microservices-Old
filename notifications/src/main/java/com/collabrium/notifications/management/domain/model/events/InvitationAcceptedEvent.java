package com.collabrium.notifications.management.domain.model.events;

public record InvitationAcceptedEvent(
    String groupName,
    String groupDescription,
    String groupImgUrl,
    String groupCode,
    String memberEmail
) {
}