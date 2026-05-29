package com.collabrium.requests.management.interfaces.rest.controllers;

import com.collabrium.requests.management.application.internal.commandservices.RequestDetailsCommandService;
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

  public RequestDetailsController(
      RequestDetailsCommandService requestDetailsCommandService
  ) {

    this.requestDetailsCommandService = requestDetailsCommandService;
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
}