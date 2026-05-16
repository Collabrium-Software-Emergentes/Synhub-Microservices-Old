package com.collabrium.groups.management.domain.model.events;

public record MemberLeftEvent(
    Long memberId,
    Long groupId
) {
}