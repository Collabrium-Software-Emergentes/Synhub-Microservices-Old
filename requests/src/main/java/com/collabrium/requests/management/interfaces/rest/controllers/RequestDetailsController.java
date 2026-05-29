package com.collabrium.requests.management.interfaces.rest.controllers;

import com.collabrium.requests.management.application.internal.commandservices.RequestDetailsCommandService;
import com.collabrium.requests.management.application.internal.queryservices.RequestDetailsQueryService;
import com.collabrium.requests.management.domain.model.queries.GetRequestDetailsByIdQuery;
import com.collabrium.requests.management.domain.model.queries.GetRequestsDetailsByTaskIdQuery;
import com.collabrium.requests.management.interfaces.rest.resources.CreateRequestResource;
import com.collabrium.requests.management.interfaces.rest.resources.RequestDetailsResource;
import com.collabrium.requests.management.interfaces.rest.transform.CreateRequestCommandFromResourceAssembler;
import com.collabrium.requests.management.interfaces.rest.transform.RequestDetailsResourceFromDTOAssembler;
import com.collabrium.requests.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
    value = "/api/v1/tasks/{taskId}/requests",
    produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Request",
    description = "Request management API"
)
public class RequestDetailsController {

  private final RequestDetailsCommandService requestDetailsCommandService;
  private final RequestDetailsQueryService requestDetailsQueryService;

  public RequestDetailsController(
      RequestDetailsCommandService requestDetailsCommandService,
      RequestDetailsQueryService requestDetailsQueryService
  ) {

    this.requestDetailsCommandService = requestDetailsCommandService;
    this.requestDetailsQueryService = requestDetailsQueryService;
  }

  @PostMapping
  @Operation(summary = "Create a new request", description = "Create a new request")
  public ResponseEntity<RequestDetailsResource> createRequest(
      @PathVariable Long taskId,
      @RequestBody CreateRequestResource resource,
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var createRequestCommand =
        CreateRequestCommandFromResourceAssembler
            .toCommandFromResource(resource, taskId, user.userId());

    var requestDetails = requestDetailsCommandService.handle(createRequestCommand);

    if (requestDetails.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var requestDetailsResource = RequestDetailsResourceFromDTOAssembler.toResourceFromDTO(requestDetails.get());

    return ResponseEntity.ok(requestDetailsResource);
  }

  @GetMapping
  @Operation(summary = "Get requests from a task", description = "Get a list of requests from a task id")
  public ResponseEntity<List<RequestDetailsResource>> getRequestsByTaskId(
      @PathVariable Long taskId
  ) {

    var getRequestsByTaskIdQuery = new GetRequestsDetailsByTaskIdQuery(taskId);

    var requests = requestDetailsQueryService.handle(getRequestsByTaskIdQuery);

    var requestsResources = requests
        .stream()
        .map(RequestDetailsResourceFromDTOAssembler::toResourceFromDTO)
        .toList();

    return ResponseEntity.ok(requestsResources);
  }

  @GetMapping("/{requestId}")
  @Operation(summary = "Get a request by id", description = "Get a request by id")
  public ResponseEntity<RequestDetailsResource> getRequestById(
      @PathVariable Long taskId,
      @PathVariable Long requestId
  ) {

    var getRequestDetailsByIdQuery = new GetRequestDetailsByIdQuery(taskId, requestId);

    var requestDetails = requestDetailsQueryService.handle(getRequestDetailsByIdQuery);

    if (requestDetails.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var requestDetailsResource =
        RequestDetailsResourceFromDTOAssembler
            .toResourceFromDTO(requestDetails.get());

    return ResponseEntity.ok(requestDetailsResource);
  }
}