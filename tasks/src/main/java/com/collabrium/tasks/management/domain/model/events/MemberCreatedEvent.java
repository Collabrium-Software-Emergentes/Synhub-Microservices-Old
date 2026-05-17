package com.collabrium.tasks.management.domain.model.events;

public record MemberCreatedEvent(
    Long userId,
    Long memberId
) {
}