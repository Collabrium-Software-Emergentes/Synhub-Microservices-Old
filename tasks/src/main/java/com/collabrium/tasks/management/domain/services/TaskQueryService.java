package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.queries.GetTaskByIdQuery;

import java.util.Optional;

public interface TaskQueryService {

  /**
   * Retrieves tasks assigned to a specific member.
   */
  Optional<Task> handle(GetTaskByIdQuery query);
}