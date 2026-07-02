package com.collabrium.groups.management.domain.model.events;

public record GroupMemberInfo(
    Long memberId,
    String email
) {
}