package com.collabrium.tasks.management.domain.model.events;

public record MemberRemovedFromGroupEvent(
    Long memberId
) {
}