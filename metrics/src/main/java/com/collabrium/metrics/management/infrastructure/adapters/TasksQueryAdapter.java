package com.collabrium.metrics.management.infrastructure.adapters;

import com.collabrium.metrics.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.metrics.shared.infrastructure.clients.tasks.TasksFeignClient;
import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;
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
  public List<TaskOnlyResource> getTasksByMemberId(
      Long memberId
  ) {

    return client.getTasksByMemberId(memberId);
  }

  @Override
  public List<TaskOnlyResource> getSimpleTasksByGroupId(
    Long groupId
  ) {

    return client.getSimpleTasksByGroupId(groupId);
  }
}