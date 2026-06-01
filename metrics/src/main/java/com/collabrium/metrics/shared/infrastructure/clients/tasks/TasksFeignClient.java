package com.collabrium.metrics.shared.infrastructure.clients.tasks;

import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "tasks-service")
public interface TasksFeignClient {

  @GetMapping("/api/v1/tasks")
  List<TaskOnlyResource> getTasksByMemberId(
      @RequestParam Long memberId
  );
}