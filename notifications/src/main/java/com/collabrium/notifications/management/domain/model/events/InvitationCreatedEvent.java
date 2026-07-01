package com.collabrium.notifications.management.domain.model.events;

public record InvitationCreatedEvent(
    String leaderEmail,
    String memberUsername,
    String memberName,
    String memberSurname,
    String memberImgUrl,
    String memberEmail
) {
}