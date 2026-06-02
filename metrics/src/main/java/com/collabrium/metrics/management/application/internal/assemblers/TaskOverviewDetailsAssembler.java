package com.collabrium.metrics.management.application.internal.assemblers;

import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskOverviewDetailsAssembler {

  private TaskOverviewDetailsAssembler() {
  }

  public static Map<String, Integer> fromTasks(
    List<TaskOnlyResource> tasks
  ) {

    return tasks.stream()
      .collect(
        Collectors.groupingBy(
          TaskOnlyResource::status,
          Collectors.summingInt(task -> 1)
        )
      );
  }
}