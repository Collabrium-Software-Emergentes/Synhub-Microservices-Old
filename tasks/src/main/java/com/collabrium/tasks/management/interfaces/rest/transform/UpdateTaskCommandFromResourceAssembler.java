package com.collabrium.tasks.management.interfaces.rest.transform;

import com.collabrium.tasks.management.domain.model.commands.UpdateTaskCommand;
import com.collabrium.tasks.management.interfaces.rest.resources.UpdateTaskResource;
import org.springframework.web.multipart.MultipartFile;

public class UpdateTaskCommandFromResourceAssembler {

  private UpdateTaskCommandFromResourceAssembler() {
  }

  public static UpdateTaskCommand toCommandFromResource(
          UpdateTaskResource resource,
          Long taskId,
          Long userId,
          MultipartFile file
  ) {

    return new UpdateTaskCommand(
            taskId,
            resource.title(),
            resource.description(),
            resource.dueDate(),
            resource.memberId(),
            userId,
            file
    );
  }
}