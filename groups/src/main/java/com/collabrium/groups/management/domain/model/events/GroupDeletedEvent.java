package com.collabrium.groups.management.domain.model.events;

import java.util.List;

public record GroupDeletedEvent(
    Long groupId,
    String publicId,
    String groupName,
    String groupDescription,
    String groupImageUrl,
    String groupCode,
    String leaderEmail,
    List<GroupMemberInfo> members
) {
}