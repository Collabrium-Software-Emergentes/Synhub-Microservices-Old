package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.application.internal.commandservices.TaskDetailsCommandService;
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

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "Tasks Member ", description = "Tasks Member endpoints")
public class MemberTaskDetailsController {

  private final TaskDetailsCommandService taskDetailsCommandService;

  public MemberTaskDetailsController(
      TaskDetailsCommandService taskDetailsCommandService
  ) {

    this.taskDetailsCommandService = taskDetailsCommandService;
  }

  @PostMapping("/{memberId}/tasks")
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
}
