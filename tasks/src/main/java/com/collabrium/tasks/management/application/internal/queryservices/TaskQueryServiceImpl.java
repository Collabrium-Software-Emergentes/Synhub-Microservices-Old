package com.collabrium.tasks.management.application.internal.queryservices;

import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.queries.*;
import com.collabrium.tasks.management.domain.services.TaskQueryService;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskQueryServiceImpl implements TaskQueryService {

  private final TaskRepository taskRepository;

  public TaskQueryServiceImpl(
      TaskRepository taskRepository
  ) {

    this.taskRepository = taskRepository;
  }

  @Override
  public List<Task> handle(GetAllTasksQuery query) {

    return taskRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Task> handle(GetTaskByIdQuery query) {

    return taskRepository.findById(query.taskId());
  }
}