package com.collabrium.tasks.management.interfaces.messaging.transform;

import com.collabrium.tasks.management.domain.model.commands.CreateMemberCommand;
import com.collabrium.tasks.management.domain.model.events.UserMemberCreatedEvent;

public class CreateMemberCommandFromEventAssembler {

  private CreateMemberCommandFromEventAssembler() {
  }

  public static CreateMemberCommand toCommandFromEvent(
      UserMemberCreatedEvent event
  ) {

    return new CreateMemberCommand(
        event.userId()
    );
  }
}