package com.collabrium.requests.management.application.internal.outboundservices.ports;

import com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource;

import java.util.List;

public interface TasksQueryPort {

  TaskResource getTaskDetailsById(Long taskId);

  List<TaskResource> getTasksDetailsByGroupId(Long groupId);

  List<TaskResource> getTasksDetailsByMemberId(Long memberId);
}