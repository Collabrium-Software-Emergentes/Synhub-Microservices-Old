package com.collabrium.requests.management.application.internal.outboundservices.ports;

import com.collabrium.requests.shared.infrastructure.clients.groups.resources.GroupOnlyResource;

public interface GroupsQueryPort {

  GroupOnlyResource getGroupOnlyByLeaderId(Long leaderId);

  GroupOnlyResource getGroupOnlyByGroupId(Long groupId);
}