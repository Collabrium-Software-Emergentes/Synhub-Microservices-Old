package com.collabrium.metrics.management.application.internal.outboundservices.ports;

import com.collabrium.metrics.shared.infrastructure.clients.groups.resources.GroupOnlyResource;

public interface GroupsQueryPort {

  GroupOnlyResource getGroupByLeaderId(Long leaderId);
}