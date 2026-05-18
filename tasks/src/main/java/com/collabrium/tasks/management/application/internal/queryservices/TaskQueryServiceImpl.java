package com.collabrium.tasks.management.application.internal.queryservices;

import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.queries.*;
import com.collabrium.tasks.management.domain.model.valueobjects.GroupId;
import com.collabrium.tasks.management.domain.model.valueobjects.TaskStatus;
import com.collabrium.tasks.management.domain.services.TaskQueryService;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.TaskRepository;
import org.springframework.stereotype.Service;

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
  public Optional<Task> handle(GetTaskByIdQuery query) {

    return taskRepository.findById(query.taskId());
  }

  @Override
  public List<Task> handle(GetAllTasksByMemberId query) {

    return taskRepository.findByMember_Id(query.memberId());
  }

  @Override
  public List<Task> handle(GetAllTaskByStatusQuery query) {

    TaskStatus taskStatus = TaskStatus.valueOf(query.taskStatus());

    return taskRepository.findByStatus(taskStatus);
  }

  @Override
  public List<Task> handle(GetAllTasksByGroupIdQuery query) {

    GroupId groupId = new GroupId(query.groupId());

    return taskRepository.findByGroupId(groupId);
  }

  @Override
  public Optional<Task> handle(GetTaskDetailsByIdQuery query) {

    return taskRepository.findById(query.taskId());
  }
}