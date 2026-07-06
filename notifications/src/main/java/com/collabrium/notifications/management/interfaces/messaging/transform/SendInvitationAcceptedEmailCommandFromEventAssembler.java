package com.collabrium.notifications.management.interfaces.messaging.transform;

import com.collabrium.notifications.management.domain.model.commands.SendInvitationAcceptedEmailCommand;
import com.collabrium.notifications.management.domain.model.events.InvitationAcceptedEvent;

public class SendInvitationAcceptedEmailCommandFromEventAssembler {

  private SendInvitationAcceptedEmailCommandFromEventAssembler() {
  }

  public static SendInvitationAcceptedEmailCommand toCommandFromEvent (
      InvitationAcceptedEvent event
  ) {

    return new SendInvitationAcceptedEmailCommand(
        event.groupName(),
        event.groupDescription(),
        event.groupImgUrl(),
        event.groupCode(),
        event.memberEmail()
    );
  }
}