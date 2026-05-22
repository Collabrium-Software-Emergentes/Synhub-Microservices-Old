package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.application.internal.queryservices.MemberDetailsQueryService;
import com.collabrium.tasks.management.domain.model.queries.GetMemberDetailsByUserIdQuery;
import com.collabrium.tasks.management.interfaces.rest.resources.MemberResource;
import com.collabrium.tasks.management.interfaces.rest.transform.MemberResourceFromDTOAssembler;
import com.collabrium.tasks.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@Tag(name = "Member Details", description = "Member Details Management API")
public class MemberDetailsController {

  private final MemberDetailsQueryService memberDetailsQueryService;

  public MemberDetailsController(
      MemberDetailsQueryService memberDetailsQueryService
  ) {

    this.memberDetailsQueryService = memberDetailsQueryService;
  }

  @GetMapping("/details")
  @Operation(summary = "Get member details by authentication", description = "Fetches the details of the authenticated member.")
  public ResponseEntity<MemberResource> getMemberByAuthentication(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getMemberDetailsByUserIdQuery = new GetMemberDetailsByUserIdQuery(user.userId());

    var memberDetails = memberDetailsQueryService.handle(getMemberDetailsByUserIdQuery);

    if (memberDetails.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var memberResource = MemberResourceFromDTOAssembler.toResourceFromDTO(memberDetails.get());

    return ResponseEntity.ok(memberResource);
  }
}