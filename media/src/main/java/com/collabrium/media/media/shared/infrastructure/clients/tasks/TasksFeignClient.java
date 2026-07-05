package com.collabrium.media.media.shared.infrastructure.clients.tasks;

import com.collabrium.media.media.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tasks-service")
public interface TasksFeignClient {

    @GetMapping("/api/v1/tasks/{taskId}/only")
    TaskOnlyResource getTaskById(@PathVariable Long taskId);
}