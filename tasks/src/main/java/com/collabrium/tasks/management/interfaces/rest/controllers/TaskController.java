package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.domain.model.queries.GetTaskByIdQuery;
import com.collabrium.tasks.management.domain.services.TaskQueryService;
import com.collabrium.tasks.management.interfaces.rest.resources.TaskDetailsResource;
import com.collabrium.tasks.management.interfaces.rest.transform.TaskDetailsResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/tasks")
@Tag(name = "Task", description = "Task management API")
public class TaskController {

  private final TaskQueryService taskQueryService;

  public TaskController(
      TaskQueryService taskQueryService
  ) {

    this.taskQueryService = taskQueryService;
  }

  @GetMapping("/details/{taskId}")
  @Operation(
      summary = "Get task details by id",
      description = "Get task details by id"
  )
  public ResponseEntity<TaskDetailsResource> getTaskDetailsById(
      @PathVariable Long taskId
  ) {

    var getTaskByIdQuery = new GetTaskByIdQuery(taskId);

    var task = this.taskQueryService.handle(getTaskByIdQuery);

    if (task.isEmpty()) return ResponseEntity.notFound().build();

    var taskResource = TaskDetailsResourceFromEntityAssembler.toResourceFromEntity(task.get());

    return ResponseEntity.ok(taskResource);
  }
}