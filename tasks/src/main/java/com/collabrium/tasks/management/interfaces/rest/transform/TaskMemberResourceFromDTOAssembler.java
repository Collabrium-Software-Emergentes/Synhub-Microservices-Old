package com.collabrium.tasks.management.interfaces.rest.transform;

import com.collabrium.tasks.management.application.internal.dto.TaskMemberDTO;
import com.collabrium.tasks.management.interfaces.rest.resources.TaskMemberResource;

public class TaskMemberResourceFromDTOAssembler {

  private TaskMemberResourceFromDTOAssembler() {
  }

  public static TaskMemberResource toResourceFromDTO(
      TaskMemberDTO dto
  ) {

    if (dto == null) {
      return null;
    }

    return new TaskMemberResource(
        dto.id(),
        dto.name(),
        dto.surname(),
        dto.urlImage()
    );
  }
}