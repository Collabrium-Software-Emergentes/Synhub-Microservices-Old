package com.collabrium.requests.management.interfaces.rest.controllers;

import com.collabrium.requests.management.domain.model.commands.UpdateRequestStatusCommand;
import com.collabrium.requests.management.domain.services.RequestCommandService;
import com.collabrium.requests.management.interfaces.rest.resources.RequestResource;
import com.collabrium.requests.management.interfaces.rest.transform.RequestResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/api/v1/tasks/{taskId}/requests",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(
    name = "Request",
    description = "Request management API"
)
public class RequestController {

  private final RequestCommandService requestCommandService;

  public RequestController(
      RequestCommandService requestCommandService
  ) {

    this.requestCommandService = requestCommandService;
  }

  @PutMapping("/{requestId}/status/{status}")
  @Operation(summary = "Update a request status", description = "Update the status of a request")
  public ResponseEntity<RequestResource> updateRequestStatus(
      @PathVariable Long taskId,
      @PathVariable Long requestId,
      @PathVariable String status
  ) {

    var updateRequestStatusCommand = new UpdateRequestStatusCommand(requestId, status, taskId);

    var request = requestCommandService.handle(updateRequestStatusCommand);

    if (request.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var requestResource = RequestResourceFromEntityAssembler.toResourceFromEntity(request.get());

    return ResponseEntity.ok(requestResource);
  }
}