package com.collabrium.media.media.management.application.internal.outboundservices.ports;

import com.collabrium.media.media.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;

public interface TasksQueryPort {
    TaskOnlyResource getTaskById(Long id);
}