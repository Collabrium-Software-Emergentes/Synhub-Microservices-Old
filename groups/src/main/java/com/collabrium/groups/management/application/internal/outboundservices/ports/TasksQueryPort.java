package com.collabrium.groups.management.application.internal.outboundservices.ports;

import com.collabrium.groups.shared.infrastructure.clients.tasks.resources.MemberResource;

import java.util.List;

public interface TasksQueryPort {

  List<MemberResource> getMembersByGroupId(Long groupId);
}