package com.collabrium.tasks.management.domain.model.events;

public record InvitationAcceptedEvent(
    Long groupId,
    Long memberId
) {
}