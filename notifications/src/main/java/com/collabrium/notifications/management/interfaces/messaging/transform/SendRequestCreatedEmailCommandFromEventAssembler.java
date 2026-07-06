package com.collabrium.notifications.management.interfaces.messaging.transform;

import com.collabrium.notifications.management.domain.model.commands.SendRequestCreatedEmailCommand;
import com.collabrium.notifications.management.domain.model.events.RequestCreatedEvent;

public class SendRequestCreatedEmailCommandFromEventAssembler {

  private SendRequestCreatedEmailCommandFromEventAssembler() {
  }

  public static SendRequestCreatedEmailCommand toCommandFromEvent(
      RequestCreatedEvent event
  ) {

    return new SendRequestCreatedEmailCommand(
        event.leaderEmail(),
        event.memberUsername(),
        event.memberName(),
        event.memberSurname(),
        event.taskTitle(),
        event.requestDescription(),
        event.requestType(),
        event.imageUrl()
    );
  }
}
