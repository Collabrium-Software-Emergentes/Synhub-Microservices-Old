package com.collabrium.notifications.management.interfaces.messaging.transform;

import com.collabrium.notifications.management.domain.model.commands.SendGroupDeletedEmailCommand;
import com.collabrium.notifications.management.domain.model.events.GroupDeletedEvent;
import com.collabrium.notifications.management.domain.model.events.resources.GroupMemberInfo;

import java.util.List;

public class SendGroupDeletedEmailCommandFromEventAssembler {

  private SendGroupDeletedEmailCommandFromEventAssembler() {
  }

  public static SendGroupDeletedEmailCommand toCommandFromEvent (
      GroupDeletedEvent event
  ) {

    List<String> memberEmails = event.members().stream().map(GroupMemberInfo::email).toList();

    return new SendGroupDeletedEmailCommand(
        event.groupName(),
        event.groupDescription(),
        event.groupCode(),
        event.leaderEmail(),
        memberEmails
    );
  }
}