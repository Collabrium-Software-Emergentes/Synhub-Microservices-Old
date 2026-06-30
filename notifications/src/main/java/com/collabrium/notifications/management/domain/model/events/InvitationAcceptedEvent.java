package com.collabrium.notifications.management.domain.model.events;

public record InvitationAcceptedEvent(
    String groupName,
    String groupImgUrl,
    String groupCode,
    String memberEmail
) {
}