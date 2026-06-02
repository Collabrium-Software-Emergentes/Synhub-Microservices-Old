package com.collabrium.metrics.management.application.internal.assemblers;

import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;

import java.util.List;
import java.util.Map;

public class RescheduledTasksDetailsAssembler {

  private RescheduledTasksDetailsAssembler() {
  }

  public static Map<String, Integer> fromTasks(
      List<TaskOnlyResource> tasks,
      long totalRescheduledTimes
  ) {

    return Map.of(
        "total",
        tasks.size(),
        "rescheduled",
        (int) totalRescheduledTimes
    );
  }
}