package com.collabrium.notifications.management.interfaces.messaging.transform;

import com.collabrium.notifications.management.domain.model.commands.SendInvitationCreatedEmailCommand;
import com.collabrium.notifications.management.domain.model.events.InvitationCreatedEvent;

public class SendInvitationCreatedEmailCommandFromEventAssembler {

  private SendInvitationCreatedEmailCommandFromEventAssembler() {
  }

  public static SendInvitationCreatedEmailCommand toCommandFromEvent (
      InvitationCreatedEvent event
  ) {

    return new SendInvitationCreatedEmailCommand(
        event.leaderEmail(),
        event.memberUsername(),
        event.memberName(),
        event.memberSurname(),
        event.memberImgUrl(),
        event.memberEmail()
    );
  }
}