package com.collabrium.groups.management.domain.model.events.resources;

public record GroupMemberInfo(
    Long memberId,
    String email
) {
}