package com.collabrium.notifications.management.domain.model.commands;

import java.util.List;

public record SendGroupDeletedEmailCommand(
    String groupName,
    String groupDescription,
    String groupCode,
    String leaderEmail,
    List<String> membersEmails
) {
}