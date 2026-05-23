package com.collabrium.tasks.management.interfaces.messaging.transform;

import com.collabrium.tasks.management.domain.model.commands.AssignMemberToGroupCommand;
import com.collabrium.tasks.management.domain.model.events.InvitationAcceptedEvent;

public class AssignMemberToGroupCommandFromEventAssembler {

  private AssignMemberToGroupCommandFromEventAssembler() {
  }

  public static AssignMemberToGroupCommand toCommandFromEvent(
      InvitationAcceptedEvent event
  ) {

    return new AssignMemberToGroupCommand(
        event.memberId(),
        event.groupId()
    );
  }
}