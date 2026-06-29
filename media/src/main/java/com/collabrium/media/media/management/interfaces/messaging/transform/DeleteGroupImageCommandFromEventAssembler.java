package com.collabrium.media.media.management.interfaces.messaging.transform;

import com.collabrium.media.media.management.domain.model.commands.DeleteGroupImageCommand;
import com.collabrium.media.media.management.domain.model.events.GroupDeletedEvent;

public class DeleteGroupImageCommandFromEventAssembler {

  private DeleteGroupImageCommandFromEventAssembler() {
  }

  public static DeleteGroupImageCommand toCommandFromEvent(
      GroupDeletedEvent event
  ) {

    return new DeleteGroupImageCommand(
        event.groupId()
    );
  }
}