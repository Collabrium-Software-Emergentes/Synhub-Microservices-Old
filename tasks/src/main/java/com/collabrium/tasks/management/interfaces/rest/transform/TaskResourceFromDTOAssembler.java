package com.collabrium.tasks.management.interfaces.rest.transform;

import com.collabrium.tasks.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.tasks.management.interfaces.rest.resources.TaskResource;

public class TaskResourceFromDTOAssembler {

  private TaskResourceFromDTOAssembler() {
  }

  public static TaskResource toResourceFromDTO(
      TaskDetailsDTO dto
  ) {

    return new TaskResource(
        dto.id(),
        dto.title(),
        dto.description(),
        dto.dueDate(),
        dto.createdAt(),
        dto.updatedAt(),
        dto.status(),
        dto.timesRearranged(),
        dto.timePassed(),
        TaskMemberResourceFromDTOAssembler
            .toResourceFromDTO(dto.member()),
        dto.groupId(),
        dto.imageUrl()
    );
  }
}