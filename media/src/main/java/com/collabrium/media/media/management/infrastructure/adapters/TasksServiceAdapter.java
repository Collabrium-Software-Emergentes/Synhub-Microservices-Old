package com.collabrium.media.media.management.infrastructure.adapters;

import com.collabrium.media.media.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.media.media.shared.infrastructure.clients.tasks.TasksFeignClient;
import com.collabrium.media.media.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;
import org.springframework.stereotype.Component;

@Component
public class TasksServiceAdapter implements TasksQueryPort {

    private final TasksFeignClient client;

    public TasksServiceAdapter(TasksFeignClient client) {
        this.client = client;
    }

    @Override
    public TaskOnlyResource getTaskById(Long id) {
        return client.getTaskById(id);
    }
}