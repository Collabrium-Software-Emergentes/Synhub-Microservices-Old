package com.collabrium.notifications.management.interfaces.messaging.transform;

import com.collabrium.notifications.management.domain.model.commands.SendGroupCreatedEmailCommand;
import com.collabrium.notifications.management.domain.model.events.GroupCreatedEvent;

public class SendGroupCreatedEmailCommandFromEventAssembler {

  private SendGroupCreatedEmailCommandFromEventAssembler() {
  }

  public static SendGroupCreatedEmailCommand toCommandFromEvent (
      GroupCreatedEvent event
  ) {

    return new SendGroupCreatedEmailCommand(
        event.groupName(),
        event.groupDescription(),
        event.imgUrl(),
        event.code(),
        event.leaderEmail()
    );
  }
}