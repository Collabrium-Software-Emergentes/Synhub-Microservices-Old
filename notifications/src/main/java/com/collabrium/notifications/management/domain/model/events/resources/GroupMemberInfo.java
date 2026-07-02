package com.collabrium.notifications.management.domain.model.events.resources;

public record GroupMemberInfo(
    Long memberId,
    String email
) {
}