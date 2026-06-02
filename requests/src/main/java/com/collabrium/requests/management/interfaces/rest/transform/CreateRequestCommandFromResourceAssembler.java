package com.collabrium.requests.management.interfaces.rest.transform;

import com.collabrium.requests.management.domain.model.commands.CreateRequestCommand;
import com.collabrium.requests.management.interfaces.rest.resources.CreateRequestResource;

public class CreateRequestCommandFromResourceAssembler {

  private CreateRequestCommandFromResourceAssembler() {
  }

  public static CreateRequestCommand toCommandFromResource(
      CreateRequestResource resource,
      Long taskId,
      Long userId
  ) {

    return new CreateRequestCommand(
        resource.description(),
        resource.requestType(),
        taskId,
        userId
    );
  }
}