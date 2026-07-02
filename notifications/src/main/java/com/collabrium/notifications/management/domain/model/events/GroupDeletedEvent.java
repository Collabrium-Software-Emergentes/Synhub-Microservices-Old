package com.collabrium.notifications.management.domain.model.events;

import com.collabrium.notifications.management.domain.model.events.resources.GroupMemberInfo;

import java.util.List;

public record GroupDeletedEvent(
    String groupName,
    String groupDescription,
    String groupImageUrl,
    String groupCode,
    String leaderEmail,
    List<GroupMemberInfo> members
) {
}