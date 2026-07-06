package com.collabrium.media.media.management.application.internal.outboundservices.ports;

import com.collabrium.media.media.shared.infrastructure.clients.groups.resources.GroupOnlyResource;

public interface GroupsQueryPort {

  GroupOnlyResource getGroupById(Long id);
}