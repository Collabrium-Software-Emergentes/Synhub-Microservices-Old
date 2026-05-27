package com.collabrium.tasks.management.interfaces.messaging.transform;

import com.collabrium.tasks.management.domain.model.commands.DeleteGroupDataCommand;
import com.collabrium.tasks.management.domain.model.events.GroupDeletedEvent;

public class DeleteGroupDataCommandFromEventAssembler {

  private DeleteGroupDataCommandFromEventAssembler() {
  }

  public static DeleteGroupDataCommand toCommandFromEvent(
      GroupDeletedEvent event
  ) {

    return new DeleteGroupDataCommand(
        event.groupId()
    );
  }
}