package com.collabrium.tasks.management.interfaces.rest.transform;

import com.collabrium.tasks.management.domain.model.commands.UpdateTaskCommand;
import com.collabrium.tasks.management.interfaces.rest.resources.UpdateTaskResource;

public class UpdateTaskCommandFromResourceAssembler {

  private UpdateTaskCommandFromResourceAssembler() {
  }

  public static UpdateTaskCommand toCommandFromResource(
      UpdateTaskResource resource,
      Long taskId
  ) {

    return new UpdateTaskCommand(
        taskId,
        resource.title(),
        resource.description(),
        resource.dueDate(),
        resource.memberId()
    );
  }
}