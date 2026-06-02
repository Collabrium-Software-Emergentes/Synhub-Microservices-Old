package com.collabrium.requests.management.infrastructure.adapters;

import com.collabrium.requests.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.requests.shared.infrastructure.clients.tasks.TasksFeignClient;
import com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TasksQueryAdapter implements TasksQueryPort {

  private final TasksFeignClient client;

  public TasksQueryAdapter(
      TasksFeignClient client
  ) {

    this.client = client;
  }

  @Override
  public TaskResource getTaskDetailsById(Long taskId) {
    return client.getTaskDetailsById(taskId);
  }

  @Override
  public List<TaskResource> getTasksDetailsByGroupId(Long groupId) {
    return client.getAllTasksByGroupId(groupId);
  }

  @Override
  public List<TaskResource> getTasksDetailsByMemberId(Long memberId) {
    return client.getAllTasksByMemberId(memberId);
  }
}