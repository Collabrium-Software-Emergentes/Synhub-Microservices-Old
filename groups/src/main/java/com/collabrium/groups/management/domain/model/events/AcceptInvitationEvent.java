package com.collabrium.groups.management.domain.model.events;

public record AcceptInvitationEvent(
    Long groupId,
    Long memberId
) {
}