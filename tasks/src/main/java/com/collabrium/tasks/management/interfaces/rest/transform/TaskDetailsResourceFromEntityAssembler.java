package com.collabrium.tasks.management.interfaces.rest.transform;

import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.interfaces.rest.resources.TaskDetailsResource;

public class TaskDetailsResourceFromEntityAssembler {

  private TaskDetailsResourceFromEntityAssembler() {
  }

  public static TaskDetailsResource toResourceFromEntity(
      Task entity
  ) {

    return new TaskDetailsResource(
        entity.getId(),
        entity.getTitle(),
        entity.getDescription(),
        entity.getDueDate().toString(),
        entity.getCreatedAt().toString(),
        entity.getUpdatedAt().toString(),
        entity.getStatus().toString(),
        entity.getTimesRearranged(),
        entity.getTimePassed(),
        entity.getMember().getId(),
        entity.getGroupId().value()
    );
  }
}