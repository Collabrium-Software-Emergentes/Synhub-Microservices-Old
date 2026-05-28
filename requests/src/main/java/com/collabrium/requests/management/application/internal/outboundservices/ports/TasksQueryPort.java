package com.collabrium.requests.management.application.internal.outboundservices.ports;

import com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource;

public interface TasksQueryPort {

  TaskResource getTaskDetailsById(Long taskId);
}