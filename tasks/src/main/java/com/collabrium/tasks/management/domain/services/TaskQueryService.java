package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.queries.GetTaskByIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetTasksByGroupIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetTasksByMemberIdQuery;

import java.util.List;
import java.util.Optional;

public interface TaskQueryService {

  /**
   * Retrieves tasks assigned to a specific member.
   */
  Optional<Task> handle(GetTaskByIdQuery query);

  List<Task> handle(GetTasksByMemberIdQuery query);

  List<Task> handle(GetTasksByGroupIdQuery query);
}