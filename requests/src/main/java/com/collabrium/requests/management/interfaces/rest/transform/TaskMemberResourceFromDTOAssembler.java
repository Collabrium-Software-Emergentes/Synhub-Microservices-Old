package com.collabrium.requests.management.interfaces.rest.transform;

import com.collabrium.requests.management.application.internal.dto.TaskMemberDetailsDTO;
import com.collabrium.requests.management.interfaces.rest.resources.TaskMemberResource;

public class TaskMemberResourceFromDTOAssembler {

  private TaskMemberResourceFromDTOAssembler() {
  }

  public static TaskMemberResource toResourceFromDTO(
      TaskMemberDetailsDTO dto
  ) {

    return new TaskMemberResource(
        dto.id(),
        dto.name(),
        dto.surname(),
        dto.urlImage()
    );
  }
}