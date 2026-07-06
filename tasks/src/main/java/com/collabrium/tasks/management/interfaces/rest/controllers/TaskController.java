package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.domain.model.commands.DeleteTaskCommand;
import com.collabrium.tasks.management.domain.model.queries.GetTaskByIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetTasksByGroupIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetTasksByMemberIdQuery;
import com.collabrium.tasks.management.domain.services.TaskCommandService;
import com.collabrium.tasks.management.domain.services.TaskQueryService;
import com.collabrium.tasks.management.interfaces.rest.resources.TaskDetailsResource;
import com.collabrium.tasks.management.interfaces.rest.resources.TaskOnlyResource;
import com.collabrium.tasks.management.interfaces.rest.transform.TaskDetailsResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tasks")
@Tag(name = "Task", description = "Task management API")
public class TaskController {

  private final TaskQueryService taskQueryService;
  private final TaskCommandService taskCommandService;

  public TaskController(
      TaskQueryService taskQueryService,
      TaskCommandService taskCommandService
  ) {

    this.taskQueryService = taskQueryService;
    this.taskCommandService = taskCommandService;
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

  @GetMapping("/{taskId}/only")
  public ResponseEntity<TaskOnlyResource> getTaskOnlyById(
          @PathVariable Long taskId
  ) {

    var query = new GetTaskByIdQuery(taskId);
    var task = taskQueryService.handle(query);

    if (task.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var resource = new TaskOnlyResource(
            task.get().getId(),
            task.get().getPublicId()
    );

    return ResponseEntity.ok(resource);
  }

  @DeleteMapping("/{taskId}")
  @Operation(
      summary = "Delete a task by id",
      description = "Delete a task by id"
  )
  public ResponseEntity<Void> deleteTask(
      @PathVariable Long taskId
  ) {

    var deleteTaskCommand = new DeleteTaskCommand(taskId);
    
    this.taskCommandService.handle(deleteTaskCommand);

    return ResponseEntity.noContent().build();
  }

  @GetMapping(
      params = "memberId"
  )
  @Operation(
      summary = "Get tasks by member id",
      description = "Get all tasks assigned to a member"
  )

  public ResponseEntity<List<TaskDetailsResource>> getTasksByMemberId(
      @RequestParam Long memberId
  ) {

    var query =
        new GetTasksByMemberIdQuery(
            memberId
        );

    var tasks =
        taskQueryService.handle(query);

    var resources =
        tasks.stream()
            .map(
                TaskDetailsResourceFromEntityAssembler
                    ::toResourceFromEntity
            )
            .toList();

    return ResponseEntity.ok(resources);
  }

  @GetMapping("/simple")
  @Operation(
    summary = "Get simple tasks by groupId",
    description = "Returns basic task information (without extra details) filtered by groupId"
  )
  public ResponseEntity<List<TaskDetailsResource>> getSimpleTasksByGroupId(
    @RequestParam Long groupId
  ) {

    var getTasksByGroupIdQuery = new GetTasksByGroupIdQuery(groupId);

    var tasks = taskQueryService.handle(getTasksByGroupIdQuery);

    var resources = tasks.stream()
        .map(TaskDetailsResourceFromEntityAssembler::toResourceFromEntity)
        .toList();

    return ResponseEntity.ok(resources);

  }
}
