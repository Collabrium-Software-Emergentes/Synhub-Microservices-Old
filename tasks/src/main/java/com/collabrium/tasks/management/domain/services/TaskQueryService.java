package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.queries.GetAllTasksByGroupIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetAllTasksQuery;
import com.collabrium.tasks.management.domain.model.queries.GetTaskByIdQuery;

import java.util.List;
import java.util.Optional;

public interface TaskQueryService {

  /**
   * Retrieves all tasks.
   */
  List<Task> handle(GetAllTasksQuery query);

  /**
   * Retrieves tasks assigned to a specific member.
   */
  Optional<Task> handle(GetTaskByIdQuery query);

  /**
   * Retrieves tasks by their group ID.
   */
  List<Task> handle(GetAllTasksByGroupIdQuery query);
}