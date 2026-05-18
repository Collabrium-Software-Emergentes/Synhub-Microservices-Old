package com.collabrium.tasks.management.interfaces.rest.transform;

import com.collabrium.tasks.management.domain.model.commands.CreateTaskCommand;
import com.collabrium.tasks.management.interfaces.rest.resources.CreateTaskResource;

public class CreateTaskCommandFromResourceAssembler {

  private CreateTaskCommandFromResourceAssembler() {
  }

  public static CreateTaskCommand toCommandFromResource(
      CreateTaskResource resource,
      Long memberId
  ) {

    return new CreateTaskCommand(
        resource.title(),
        resource.description(),
        resource.dueDate(),
        memberId
    );
  }
}