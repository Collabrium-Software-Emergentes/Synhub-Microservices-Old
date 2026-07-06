package com.collabrium.requests.management.infrastructure.adapters;

import com.collabrium.requests.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.requests.shared.infrastructure.clients.groups.GroupsFeignClient;
import com.collabrium.requests.shared.infrastructure.clients.groups.resources.GroupOnlyResource;
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
  public GroupOnlyResource getGroupOnlyByLeaderId(Long leaderId) {
    return client.getGroupOnlyByLeaderId(leaderId);
  }

  @Override
  public GroupOnlyResource getGroupOnlyByGroupId(Long groupId) {
    return client.getGroupOnlyByGroupId(groupId);
  }
}