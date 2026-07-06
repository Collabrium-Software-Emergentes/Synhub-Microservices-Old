package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.application.internal.commandservices.TaskDetailsCommandService;
import com.collabrium.tasks.management.application.internal.queryservices.TaskDetailsQueryService;
import com.collabrium.tasks.management.domain.model.commands.UpdateTaskStatusCommand;
import com.collabrium.tasks.management.domain.model.queries.*;
import com.collabrium.tasks.management.interfaces.rest.resources.CreateTaskResource;
import com.collabrium.tasks.management.interfaces.rest.resources.TaskResource;
import com.collabrium.tasks.management.interfaces.rest.resources.UpdateTaskResource;
import com.collabrium.tasks.management.interfaces.rest.transform.CreateTaskCommandFromResourceAssembler;
import com.collabrium.tasks.management.interfaces.rest.transform.TaskResourceFromDTOAssembler;
import com.collabrium.tasks.management.interfaces.rest.transform.UpdateTaskCommandFromResourceAssembler;
import com.collabrium.tasks.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
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

  @PostMapping(
          value = "/members/{memberId}/tasks",
          consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @Operation(
          summary = "Create a new task",
          description = "Creates a new task"
  )
  public ResponseEntity<TaskResource> createTask(
          @PathVariable Long memberId,
          @RequestParam String title,
          @RequestParam String description,
          @RequestParam OffsetDateTime dueDate,
          @RequestParam(value = "file", required = false) MultipartFile file,
          @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var resource = new CreateTaskResource(title, description, dueDate);

    var createTaskCommand = CreateTaskCommandFromResourceAssembler
            .toCommandFromResource(resource, memberId, user.userId(), file);

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

  @GetMapping("/members/{memberId}/tasks/next")
  @Operation(
      summary = "Get the next task by member id",
      description = "Get the next task by member id"
  )
  public ResponseEntity<TaskResource> getLastNextByMemberId(
      @PathVariable Long memberId
  ) {

    var getNextTaskDetailsByMemberIdQuery = new GetNextTaskDetailsByMemberIdQuery(memberId);

    var task = taskDetailsQueryService.handle(getNextTaskDetailsByMemberIdQuery);

    if (task.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var resource = TaskResourceFromDTOAssembler.toResourceFromDTO(task.get());

    return ResponseEntity.ok(resource);
  }

  @GetMapping(
      value = "/tasks",
      params = "groupId"
  )
  @Operation(
      summary = "Get all tasks by groupId",
      description = "Get all tasks by groupId"
  )
  public ResponseEntity<List<TaskResource>> getTasksByGroupId(
      @RequestParam Long groupId
  ) {

    var getAllTasksDetailsByGroupIdQuery = new GetAllTasksDetailsByGroupIdQuery(groupId);

    var tasks = taskDetailsQueryService.handle(getAllTasksDetailsByGroupIdQuery);

    var resources =
        tasks.stream()
            .map(TaskResourceFromDTOAssembler::toResourceFromDTO)
            .toList();

    return ResponseEntity.ok(resources);
  }

  @PutMapping(
          value = "/tasks/{taskId}",
          consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @Operation(
          summary = "Update task",
          description = "Update task"
  )
  public ResponseEntity<TaskResource> updateTask(
          @PathVariable Long taskId,
          @RequestParam(required = false) String title,
          @RequestParam(required = false) String description,
          @RequestParam(required = false) OffsetDateTime dueDate,
          @RequestParam(required = false) Long memberId,
          @RequestParam(value = "file", required = false) MultipartFile file,
          @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var resource = new UpdateTaskResource(title, description, dueDate, memberId);

    var updateTaskCommand = UpdateTaskCommandFromResourceAssembler
            .toCommandFromResource(resource, taskId, user.userId(), file);

    var task = taskDetailsCommandService.handle(updateTaskCommand);

    if (task.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var taskResource = TaskResourceFromDTOAssembler.toResourceFromDTO(task.get());

    return ResponseEntity.ok(taskResource);
  }

  @PutMapping("/tasks/{taskId}/status/{status}")
  @Operation(summary = "Update task status", description = "Update task status")
  public ResponseEntity<TaskResource> updateTaskStatus(
      @PathVariable Long taskId,
      @PathVariable String status,
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var updateTaskStatusCommand = new UpdateTaskStatusCommand(taskId, status, user.userId());

    var task = taskDetailsCommandService.handle(updateTaskStatusCommand);

    if (task.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var taskResource = TaskResourceFromDTOAssembler.toResourceFromDTO(task.get());

    return ResponseEntity.ok(taskResource);
  }
}
