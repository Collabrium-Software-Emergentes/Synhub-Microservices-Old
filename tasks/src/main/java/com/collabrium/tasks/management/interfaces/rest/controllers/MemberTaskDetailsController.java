package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.application.internal.commandservices.TaskDetailsCommandService;
import com.collabrium.tasks.management.application.internal.queryservices.TaskDetailsQueryService;
import com.collabrium.tasks.management.domain.model.queries.GetAllTasksDetailsByUserIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetNextTaskDetailsByUserIdQuery;
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
@Tag(name = "Tasks Member ", description = "Tasks Member endpoints")
public class MemberTaskDetailsController {

  private final TaskDetailsCommandService taskDetailsCommandService;
  private final TaskDetailsQueryService taskDetailsQueryService;

  public MemberTaskDetailsController(
      TaskDetailsCommandService taskDetailsCommandService,
      TaskDetailsQueryService taskDetailsQueryService
  ) {

    this.taskDetailsCommandService = taskDetailsCommandService;
    this.taskDetailsQueryService = taskDetailsQueryService;
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
}