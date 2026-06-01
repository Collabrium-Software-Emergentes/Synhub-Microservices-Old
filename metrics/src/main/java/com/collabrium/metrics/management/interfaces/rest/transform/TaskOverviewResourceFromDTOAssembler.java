package com.collabrium.metrics.management.interfaces.rest.transform;

import com.collabrium.metrics.management.application.internal.dto.TaskOverviewDTO;
import com.collabrium.metrics.management.interfaces.rest.resources.TaskOverviewResource;

public class TaskOverviewResourceFromDTOAssembler {

  private TaskOverviewResourceFromDTOAssembler() {
  }

  public static TaskOverviewResource toResourceFromDTO(
      TaskOverviewDTO dto
  ) {

    return new TaskOverviewResource(
        dto.type(),
        dto.value(),
        dto.details()
    );
  }
}