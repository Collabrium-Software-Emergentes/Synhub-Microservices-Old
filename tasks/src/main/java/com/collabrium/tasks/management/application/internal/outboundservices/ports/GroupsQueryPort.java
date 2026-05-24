package com.collabrium.tasks.management.application.internal.outboundservices.ports;

import com.collabrium.tasks.shared.infrastructure.clients.groups.resources.GroupOnlyResource;

public interface GroupsQueryPort {

  GroupOnlyResource getGroupOnlyById(Long id);

  GroupOnlyResource getGroupByLeaderId(Long leaderId);
}