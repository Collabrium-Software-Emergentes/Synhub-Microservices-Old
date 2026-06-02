package com.collabrium.metrics.management.interfaces.rest.transform;

import com.collabrium.metrics.management.application.internal.dto.RescheduledTasksDTO;
import com.collabrium.metrics.management.interfaces.rest.resources.RescheduledTasksResource;

public class RescheduledTasksResourceFromDTOAssembler {

  private RescheduledTasksResourceFromDTOAssembler() {
  }

  public static RescheduledTasksResource toResourceFromDTO(
      RescheduledTasksDTO dto
  ) {

    return new RescheduledTasksResource(
        dto.type(),
        dto.value(),
        dto.details(),
        dto.rescheduledMemberIds()
    );
  }
}