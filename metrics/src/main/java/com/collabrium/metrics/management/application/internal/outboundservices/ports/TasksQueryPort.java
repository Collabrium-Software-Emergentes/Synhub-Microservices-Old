package com.collabrium.metrics.management.application.internal.outboundservices.ports;

import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;

import java.util.List;

public interface TasksQueryPort {

  List<TaskOnlyResource> getTasksByMemberId(Long memberId);

  List<TaskOnlyResource> getSimpleTasksByGroupId(Long groupId);
}