package com.collabrium.groups.management.infrastructure.adapters;

import com.collabrium.groups.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.groups.shared.infrastructure.clients.tasks.TasksFeignClient;
import com.collabrium.groups.shared.infrastructure.clients.tasks.resources.MemberResource;
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
  public List<MemberResource> getMembersByGroupId(Long groupId) {
    return client.getAllMembersByGroupId(groupId);
  }
}