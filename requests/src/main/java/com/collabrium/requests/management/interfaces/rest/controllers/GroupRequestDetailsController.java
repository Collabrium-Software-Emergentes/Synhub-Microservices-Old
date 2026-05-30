package com.collabrium.requests.management.interfaces.rest.controllers;

import com.collabrium.requests.management.application.internal.queryservices.RequestDetailsQueryService;
import com.collabrium.requests.management.domain.model.queries.GetMyRequestsAsMemberQuery;
import com.collabrium.requests.management.interfaces.rest.resources.RequestDetailsResource;
import com.collabrium.requests.management.interfaces.rest.transform.RequestDetailsResourceFromDTOAssembler;
import com.collabrium.requests.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(
  value = "/api/v1"
)
@Tag(
  name = "Group Requests",
  description = "Group Requests management API"
)
public class GroupRequestDetailsController {

  private final RequestDetailsQueryService requestDetailsQueryService;

  public GroupRequestDetailsController(
    RequestDetailsQueryService requestDetailsQueryService
  ) {

    this.requestDetailsQueryService = requestDetailsQueryService;
  }

  @GetMapping("/member/group/requests")
  @Operation(
    summary = "Get all requests from member",
    description = "Get all requests from member"
  )
  public ResponseEntity<List<RequestDetailsResource>> getAllRequestsFromMember(
    @AuthenticationPrincipal AuthenticatedUser user
  ){

    var getMyRequestsAsMemberQuery = new GetMyRequestsAsMemberQuery(user.userId());

    var requests = requestDetailsQueryService.handle(getMyRequestsAsMemberQuery);

    var requestsResources = requests
      .stream()
      .map(RequestDetailsResourceFromDTOAssembler::toResourceFromDTO)
      .toList();

    return ResponseEntity.ok(requestsResources);
  }
}