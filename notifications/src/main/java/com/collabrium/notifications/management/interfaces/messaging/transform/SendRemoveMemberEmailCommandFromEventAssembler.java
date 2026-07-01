package com.collabrium.notifications.management.interfaces.messaging.transform;

import com.collabrium.notifications.management.domain.model.commands.SendRemoveMemberEmailCommand;
import com.collabrium.notifications.management.domain.model.events.RemoveMemberEvent;

public class SendRemoveMemberEmailCommandFromEventAssembler {

  private SendRemoveMemberEmailCommandFromEventAssembler() {
  }

  public static SendRemoveMemberEmailCommand toCommandFromEvent (
      RemoveMemberEvent event
  ) {

    return new SendRemoveMemberEmailCommand(
        event.memberEmail(),
        event.groupName(),
        event.groupImageUrl(),
        event.groupCode(),
        event.leaderEmail()
    );
  }
}