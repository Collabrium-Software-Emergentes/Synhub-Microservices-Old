package com.collabrium.requests.management.interfaces.rest.transform;

import com.collabrium.requests.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.requests.management.interfaces.rest.resources.TaskResource;

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
        TaskMemberResourceFromDTOAssembler.toResourceFromDTO(dto.member()),
        dto.groupId()
    );
  }
}