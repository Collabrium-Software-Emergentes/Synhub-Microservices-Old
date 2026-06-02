package com.collabrium.metrics.management.infrastructure.adapters;

import com.collabrium.metrics.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.metrics.shared.infrastructure.clients.groups.GroupsFeignClient;
import com.collabrium.metrics.shared.infrastructure.clients.groups.resources.GroupOnlyResource;
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
  public GroupOnlyResource getGroupByLeaderId(Long leaderId) {
    return client.getGroupByLeaderId(leaderId);
  }
}