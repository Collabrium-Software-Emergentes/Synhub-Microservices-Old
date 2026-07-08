package com.collabrium.metrics.management.interfaces.rest.controllers;

import com.collabrium.metrics.management.application.internal.queryservices.MetricsQueryService;
import com.collabrium.metrics.management.application.internal.queryservices.MetricsReportService; // [NUEVO] Importar el servicio del reporte
import com.collabrium.metrics.management.domain.model.queries.*;
import com.collabrium.metrics.management.interfaces.rest.resources.*;
import com.collabrium.metrics.management.interfaces.rest.transform.*;
import com.collabrium.metrics.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders; // NUEVO
import org.springframework.http.HttpStatus; // NUEVO
import org.springframework.http.MediaType; // NUEVO
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // NUEVO
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // NUEVO
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/metrics")
@Tag(
        name = "Metrics",
        description = "Provides access to analytics and group metrics"
)
public class MetricsController {

  private final MetricsQueryService metricsQueryService;
  private final MetricsReportService metricsReportService; // [NUEVO] Inyectar dependencia

  public MetricsController(
          MetricsQueryService metricsQueryService,
          MetricsReportService metricsReportService // [NUEVO]
  ) {

    this.metricsQueryService = metricsQueryService;
    this.metricsReportService = metricsReportService; // [NUEVO]
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

  @GetMapping("/tasks/avg-completion-time")
  @Operation(
          summary = "Get average completion time for group",
          description = "Returns the average time (in days) it " +
                  "takes to complete tasks in the authenticated leader's group."
  )
  public ResponseEntity<AvgCompletionTimeResource> getAvgCompletionTime(
          @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getAvgCompletionTimeOfMyGroupQuery = new GetAvgCompletionTimeOfMyGroupQuery(user.userId());

    var avgCompletionTime = metricsQueryService.handle(getAvgCompletionTimeOfMyGroupQuery);

    if (avgCompletionTime.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = AvgCompletionTimeResourceFromDTOAssembler.toResourceFromDTO(avgCompletionTime.get());

    return ResponseEntity.ok(resource);
  }


  // acá estan los nuevos endopoints: PDF y Correo

  @GetMapping("/report/download")
  @Operation(summary = "Download metrics report in PDF")
  public ResponseEntity<byte[]> downloadReport(@AuthenticationPrincipal AuthenticatedUser user) {

    // 1. Obtener la data real del usuario usando tu query existente
    // Acá evitamos que el código trabaje con mockus(datos simulados)
    var query = new GetTasksOverviewOfMyGroupQuery(user.userId());
    var taskOverviewOpt = metricsQueryService.handle(query);

    if (taskOverviewOpt.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    // 2. Generar el PDF con la data real (ojito nuevamente xd)
    byte[] pdfBytes = metricsReportService.getPdfReport(taskOverviewOpt.get());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "Reporte_Metricas_Grupo.pdf");

    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
  }

  @PostMapping("/report/send")
  @Operation(summary = "Send metrics report via email")
  public ResponseEntity<String> sendReportToEmail(
          @RequestParam String email,
          @AuthenticationPrincipal AuthenticatedUser user
  ) {
    var query = new GetTasksOverviewOfMyGroupQuery(user.userId());
    var taskOverviewOpt = metricsQueryService.handle(query);

    if (taskOverviewOpt.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    metricsReportService.sendPdfReportByEmail(email, taskOverviewOpt.get());
    return ResponseEntity.ok("Reporte con datos reales enviado a: " + email);
  }}