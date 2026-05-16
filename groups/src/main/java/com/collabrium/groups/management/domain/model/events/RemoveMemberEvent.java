package com.collabrium.groups.management.domain.model.events;

public record RemoveMemberEvent(
    Long groupId,
    Long memberId
) {
}