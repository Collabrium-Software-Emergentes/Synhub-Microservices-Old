package com.collabrium.tasks.management.infrastructure.adapters;

import com.collabrium.tasks.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.tasks.shared.infrastructure.clients.groups.GroupsFeignClient;
import com.collabrium.tasks.shared.infrastructure.clients.groups.resources.GroupOnlyResource;
import org.springframework.stereotype.Component;

@Component
public class GroupsQueryAdapter implements GroupsQueryPort {

  private final GroupsFeignClient client;

  public GroupsQueryAdapter(
      GroupsFeignClient client
  ) {

    this.client = client;
  }

  @Override
  public GroupOnlyResource getGroupOnlyById(Long id) {
    return client.getGroupOnlyById(id);
  }

  @Override
  public GroupOnlyResource getGroupByLeaderId(Long leaderId) {
    return client.getGroupByLeaderId(leaderId);
  }
}