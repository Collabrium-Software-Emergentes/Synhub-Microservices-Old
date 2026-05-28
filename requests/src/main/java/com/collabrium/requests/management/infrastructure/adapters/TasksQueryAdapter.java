package com.collabrium.requests.management.infrastructure.adapters;

import com.collabrium.requests.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.requests.shared.infrastructure.clients.tasks.TasksFeignClient;
import com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource;
import org.springframework.stereotype.Component;

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
}