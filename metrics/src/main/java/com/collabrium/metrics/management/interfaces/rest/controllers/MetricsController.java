package com.collabrium.metrics.management.interfaces.rest.controllers;

import com.collabrium.metrics.management.application.internal.queryservices.MetricsQueryService;
import com.collabrium.metrics.management.domain.model.queries.GetAvgCompletionTimeForMemberQuery;
import com.collabrium.metrics.management.interfaces.rest.resources.AvgCompletionTimeResource;
import com.collabrium.metrics.management.interfaces.rest.transform.AvgCompletionTimeResourceFromDTOAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
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
}