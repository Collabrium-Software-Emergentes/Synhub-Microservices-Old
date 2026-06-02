package com.collabrium.requests.management.application.internal.assemblers;

import com.collabrium.requests.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.requests.management.application.internal.dto.TaskMemberDetailsDTO;
import com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource;

public class TaskDetailsDTOFromTaskResourceAssembler {

  private TaskDetailsDTOFromTaskResourceAssembler() {
  }

  public static TaskDetailsDTO toDTO(
    TaskResource task
  ) {

    var memberDetails =
      new TaskMemberDetailsDTO(
        task.member().id(),
        task.member().name(),
        task.member().surname(),
        task.member().urlImage()
      );

    return new TaskDetailsDTO(
      task.id(),
      task.title(),
      task.description(),
      task.dueDate(),
      task.createdAt(),
      task.updatedAt(),
      task.status(),
      memberDetails,
      task.groupId()
    );
  }
}