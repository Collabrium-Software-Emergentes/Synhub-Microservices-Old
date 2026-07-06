package com.collabrium.groups.management.domain.model.events;

public record RemoveMemberEvent(
    Long memberId,
    String memberEmail,
    String groupName,
    String groupImageUrl,
    String groupCode,
    String leaderEmail
) {
}