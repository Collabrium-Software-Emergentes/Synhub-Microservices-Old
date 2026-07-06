package com.collabrium.notifications.management.domain.model.commands;

public record SendInvitationCreatedEmailCommand(
    String leaderEmail,
    String memberUsername,
    String memberName,
    String memberSurname,
    String memberImgUrl,
    String memberEmail
) {
}