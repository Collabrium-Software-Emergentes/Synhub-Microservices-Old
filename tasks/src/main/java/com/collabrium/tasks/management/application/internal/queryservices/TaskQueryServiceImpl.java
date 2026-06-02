package com.collabrium.tasks.management.application.internal.queryservices;

import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.queries.GetTaskByIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetTasksByGroupIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetTasksByMemberIdQuery;
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
  @Transactional(readOnly = true)
  public Optional<Task> handle(
      GetTaskByIdQuery query
  ) {

    return taskRepository.findById(query.taskId());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Task> handle(
      GetTasksByMemberIdQuery query
  ) {

    return taskRepository.findByMember_Id(
        query.memberId()
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<Task> handle(
    GetTasksByGroupIdQuery query
  ) {

    return taskRepository
      .findByGroupId_Value(
        query.groupId()
      );
  }
}