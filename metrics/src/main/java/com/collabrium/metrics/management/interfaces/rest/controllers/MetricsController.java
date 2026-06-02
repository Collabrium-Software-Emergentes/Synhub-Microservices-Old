package com.collabrium.metrics.management.interfaces.rest.controllers;

import com.collabrium.metrics.management.application.internal.queryservices.MetricsQueryService;
import com.collabrium.metrics.management.domain.model.queries.*;
import com.collabrium.metrics.management.interfaces.rest.resources.*;
import com.collabrium.metrics.management.interfaces.rest.transform.*;
import com.collabrium.metrics.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/metrics")
@Tag(
    name = "Metrics",
    description = "Provides access to analytics and group metrics"
)
public class MetricsController {

  private final MetricsQueryService metricsQueryService;

  public MetricsController(
      MetricsQueryService metricsQueryService
  ) {

    this.metricsQueryService = metricsQueryService;
  }

  @GetMapping("/member/{memberId}/tasks/avg-completion-time")
  @Operation(
      summary = "Get average completion time for member",
      description = "Returns the average time (in days) it takes for the given member to complete tasks."
  )
  public ResponseEntity<AvgCompletionTimeResource> getAvgCompletionTimeForMember(
      @PathVariable Long memberId
  ) {

    var getAvgCompletionTimeFromMemberQuery = new GetAvgCompletionTimeForMemberQuery(memberId);

    var avgCompletionTime = metricsQueryService.handle(getAvgCompletionTimeFromMemberQuery);

    if (avgCompletionTime.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = AvgCompletionTimeResourceFromDTOAssembler
        .toResourceFromDTO(avgCompletionTime.get());

    return ResponseEntity.ok(resource);
  }

  @GetMapping("/member/{memberId}/tasks/rescheduled")
  @Operation(
      summary = "Get rescheduled tasks for member",
      description = "Returns the count of rescheduled vs non-rescheduled tasks for the given member."
  )
  public ResponseEntity<RescheduledTasksResource> getRescheduledTasksForMember(
      @PathVariable Long memberId
  ) {

    var getRescheduledTasksFromMemberQuery = new GetRescheduledTasksForMemberQuery(memberId);

    var rescheduledTasks = metricsQueryService.handle(getRescheduledTasksFromMemberQuery);

    if (rescheduledTasks.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = RescheduledTasksResourceFromDTOAssembler
        .toResourceFromDTO(rescheduledTasks.get());

    return ResponseEntity.ok(resource);
  }

  @GetMapping("/member/{memberId}/tasks/distribution")
  @Operation(
      summary = "Get task distribution for member",
      description = "Returns the number of tasks assigned to the given member.",
      tags = {"Metrics"}
  )
  public ResponseEntity<TaskDistributionResource> getTaskDistributionForMember(
      @PathVariable Long memberId
  ) {

    var getTaskDistributionForMemberQuery = new GetTaskDistributionForMemberQuery(memberId);

    var taskDistribution = metricsQueryService.handle(getTaskDistributionForMemberQuery);

    if (taskDistribution.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = TaskDistributionResourceFromDTOAssembler
        .toResourceFromDTO(taskDistribution.get());

    return ResponseEntity.ok(resource);
  }

  @GetMapping("/member/{memberId}/tasks/overview")
  @Operation(
      summary = "Get task overview for member",
      description = "Returns a summary of task statuses for the given member."
  )
  public ResponseEntity<TaskOverviewResource> getTaskOverviewForMember(
      @PathVariable Long memberId
  ) {

    var getTaskOverviewForMemberQuery = new GetTaskOverviewForMemberQuery(memberId);

    var taskOverview = metricsQueryService.handle(getTaskOverviewForMemberQuery);

    if (taskOverview.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = TaskOverviewResourceFromDTOAssembler
        .toResourceFromDTO(taskOverview.get());

    return ResponseEntity.ok(resource);
  }

  @GetMapping("/task/member/{memberId}/time-passed")
  @Operation(
    summary = "Get time passed for a member's completed task",
    description = "Returns the time passed in milliseconds for a completed task assigned to the given member."
  )
  public ResponseEntity<TaskTimePassedResource> getAverageTaskTimePassed(
    @PathVariable Long memberId
  ) {

    var getTaskTimePassedQuery = new GetTaskTimePassedQuery(memberId);

    var taskTimePassed = metricsQueryService.handle(getTaskTimePassedQuery);

    if (taskTimePassed.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = TaskTimePassedResourceFromDTOAssembler
      .toResourceFromDTO(taskTimePassed.get());

    return ResponseEntity.ok(resource);
  }

  @GetMapping("/tasks/overview")
  @Operation(
    summary = "Get task overview for group",
    description = "Returns a summary of task statuses for the authenticated leader's group."
  )
  public ResponseEntity<TaskOverviewResource> getTaskOverview(
    @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getTasksOverviewOfMyGroupQuery =
      new GetTasksOverviewOfMyGroupQuery(user.userId());

    var taskOverview = metricsQueryService.handle(getTasksOverviewOfMyGroupQuery);

    if (taskOverview.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = TaskOverviewResourceFromDTOAssembler
      .toResourceFromDTO(taskOverview.get());

    return ResponseEntity.ok(resource);
  }

  @GetMapping("/tasks/distribution")
  @Operation(
    summary = "Get task distribution for group",
    description = "Returns the number of tasks assigned to each member in the authenticated leader's group."
  )
  public ResponseEntity<TaskDistributionResource> getTaskDistribution(
    @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getTaskDistributionOfMyGroupQuery =
      new GetTaskDistributionOfMyGroupQuery(user.userId());

    var taskDistribution = metricsQueryService.handle(getTaskDistributionOfMyGroupQuery);

    if (taskDistribution.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = TaskDistributionResourceFromDTOAssembler
      .toResourceFromDTO(taskDistribution.get());

    return ResponseEntity.ok(resource);
  }

  @GetMapping("/tasks/rescheduled")
  @Operation(
      summary = "Get rescheduled tasks for group",
      description = "Returns the count of rescheduled vs non-rescheduled tasks " +
          "for the authenticated leader's group, and the memberIds " +
          "of those with rescheduled tasks."
  )
  public ResponseEntity<RescheduledTasksResource> getRescheduledTasks(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getRescheduledTasksOfMyGroupQuery =
        new GetRescheduledTasksOfMyGroupQuery(user.userId());

    var rescheduledTasks = metricsQueryService.handle(getRescheduledTasksOfMyGroupQuery);

    if (rescheduledTasks.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = RescheduledTasksResourceFromDTOAssembler
        .toResourceFromDTO(rescheduledTasks.get());

    return ResponseEntity.ok(resource);
  }
}