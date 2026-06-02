package com.collabrium.metrics.management.interfaces.rest.transform;

import com.collabrium.metrics.management.application.internal.dto.TaskTimePassedDTO;
import com.collabrium.metrics.management.interfaces.rest.resources.TaskTimePassedResource;

public class TaskTimePassedResourceFromDTOAssembler {

  private TaskTimePassedResourceFromDTOAssembler() {
  }

  public static TaskTimePassedResource toResourceFromDTO(
    TaskTimePassedDTO dto
  ) {

    return new TaskTimePassedResource(
      dto.memberId(),
      dto.timePassed()
    );
  }
}