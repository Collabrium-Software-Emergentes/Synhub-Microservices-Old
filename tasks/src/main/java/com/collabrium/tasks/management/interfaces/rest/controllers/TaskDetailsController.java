package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.application.internal.commandservices.TaskDetailsCommandService;
import com.collabrium.tasks.management.application.internal.queryservices.TaskDetailsQueryService;
import com.collabrium.tasks.management.domain.model.queries.*;
import com.collabrium.tasks.management.interfaces.rest.resources.CreateTaskResource;
import com.collabrium.tasks.management.interfaces.rest.resources.TaskResource;
import com.collabrium.tasks.management.interfaces.rest.transform.CreateTaskCommandFromResourceAssembler;
import com.collabrium.tasks.management.interfaces.rest.transform.TaskResourceFromDTOAssembler;
import com.collabrium.tasks.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Tasks Details ", description = "Tasks Details endpoints")
public class TaskDetailsController {

  private final TaskDetailsCommandService taskDetailsCommandService;
  private final TaskDetailsQueryService taskDetailsQueryService;

  public TaskDetailsController(
      TaskDetailsCommandService taskDetailsCommandService,
      TaskDetailsQueryService taskDetailsQueryService
  ) {

    this.taskDetailsCommandService = taskDetailsCommandService;
    this.taskDetailsQueryService = taskDetailsQueryService;
  }

  @GetMapping("/tasks/{taskId}")
  @Operation(
      summary = "Get a task by id",
      description = "Get a task by id"
  )
  public ResponseEntity<TaskResource> getTaskDetailsById(
      @PathVariable Long taskId
  ) {

    var getTaskDetailsByIdQuery = new GetTaskDetailsByIdQuery(taskId);

    var taskDetails = taskDetailsQueryService.handle(getTaskDetailsByIdQuery);

    if (taskDetails.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var taskResource = TaskResourceFromDTOAssembler.toResourceFromDTO(taskDetails.get());

    return ResponseEntity.ok(taskResource);
  }

  @PostMapping("/members/{memberId}/tasks")
  @Operation(
      summary = "Create a new task",
      description = "Creates a new task"
  )
  public ResponseEntity<TaskResource> createTask(
      @PathVariable Long memberId,
      @RequestBody CreateTaskResource resource,
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var createTaskCommand = CreateTaskCommandFromResourceAssembler
        .toCommandFromResource(resource, memberId, user.userId());

    var taskDetails = taskDetailsCommandService.handle(createTaskCommand);

    if (taskDetails.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var taskResource = TaskResourceFromDTOAssembler.toResourceFromDTO(taskDetails.get());

    return ResponseEntity.ok(taskResource);
  }

  @GetMapping("member/tasks")
  @Operation(
      summary = "Get all tasks by authenticated member",
      description = "Fetches all tasks for the authenticated member."
  )
  public ResponseEntity<List<TaskResource>> getTasksByMemberAuthenticated(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getAllTasksDetailsByUserIdQuery = new GetAllTasksDetailsByUserIdQuery(user.userId());

    var tasks = taskDetailsQueryService.handle(getAllTasksDetailsByUserIdQuery);

    var taskResources =
        tasks.stream()
            .map(TaskResourceFromDTOAssembler::toResourceFromDTO)
            .toList();

    return ResponseEntity.ok(taskResources);
  }

  @GetMapping("/member/tasks/next")
  @Operation(
      summary = "Get next task by authenticated member",
      description = "Fetches the nearest pending task for the authenticated member."
  )
  public ResponseEntity<TaskResource> getNextTaskByAuthenticatedMember(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var query = new GetNextTaskDetailsByUserIdQuery(user.userId());

    var task = taskDetailsQueryService.handle(query);

    if (task.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var resource = TaskResourceFromDTOAssembler
        .toResourceFromDTO(task.get());

    return ResponseEntity.ok(resource);
  }

  @GetMapping("/members/{memberId}/tasks")
  @Operation(
      summary = "Get all tasks by member id",
      description = "Get all tasks by member id"
  )
  public ResponseEntity<List<TaskResource>> getAllTasksByMemberId(
      @PathVariable Long memberId
  ) {

    var query =
        new GetTasksDetailsByMemberIdQuery(
            memberId
        );

    var tasks =
        taskDetailsQueryService.handle(query);

    var resources =
        tasks.stream()
            .map(TaskResourceFromDTOAssembler::toResourceFromDTO)
            .toList();

    return ResponseEntity.ok(resources);
  }

  @GetMapping("/tasks/status/{status}")
  @Operation(summary = "Get all tasks by status", description = "Get all tasks by status")
  public ResponseEntity<List<TaskResource>> getAllTasksByStatus(
      @PathVariable String status
  ) {

    var getAllTaskDetailsByStatusQuery = new GetAllTaskDetailsByStatusQuery(status);

    var tasks = taskDetailsQueryService.handle(getAllTaskDetailsByStatusQuery);

    var resources =
        tasks.stream()
            .map(TaskResourceFromDTOAssembler::toResourceFromDTO)
            .toList();

    return ResponseEntity.ok(resources);
  }
}