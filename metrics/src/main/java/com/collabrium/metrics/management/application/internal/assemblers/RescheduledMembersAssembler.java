package com.collabrium.metrics.management.application.internal.assemblers;

import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;

import java.util.List;

public class RescheduledMembersAssembler {

  private RescheduledMembersAssembler() {
  }

  public static List<Long> fromTasks(
      List<TaskOnlyResource> tasks
  ) {

    return tasks.stream()
        .filter(task ->
            task.timesRearranged() > 0
                && task.memberId() != null
        )
        .map(TaskOnlyResource::memberId)
        .distinct()
        .toList();
  }
}