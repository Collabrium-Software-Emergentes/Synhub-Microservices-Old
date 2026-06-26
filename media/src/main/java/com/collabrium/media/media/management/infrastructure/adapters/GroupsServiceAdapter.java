package com.collabrium.media.media.management.infrastructure.adapters;

import com.collabrium.media.media.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.media.media.shared.infrastructure.clients.groups.GroupsFeignClient;
import com.collabrium.media.media.shared.infrastructure.clients.groups.resources.GroupOnlyResource;
import org.springframework.stereotype.Component;

@Component
public class GroupsServiceAdapter implements GroupsQueryPort {

  private final GroupsFeignClient client;

  public GroupsServiceAdapter(
      GroupsFeignClient client
  ) {

    this.client = client;
  }

  @Override
  public GroupOnlyResource getGroupById(
      Long id
  ) {

    return client.getGroupById(id);
  }
}