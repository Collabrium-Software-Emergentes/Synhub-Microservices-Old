package com.collabrium.requests.shared.infrastructure.clients.tasks;

import com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tasks-service")
public interface TasksFeignClient {

  @GetMapping("/api/v1/tasks/{taskId}")
  TaskResource getTaskDetailsById(
      @PathVariable Long taskId
  );
}