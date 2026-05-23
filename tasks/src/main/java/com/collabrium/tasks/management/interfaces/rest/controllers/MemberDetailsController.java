package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.application.internal.queryservices.MemberDetailsQueryService;
import com.collabrium.tasks.management.domain.model.queries.GetExtendedGroupByUserIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetMemberDetailsByIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetMemberDetailsByUserIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetMembersDetailsByGroupIdQuery;
import com.collabrium.tasks.management.interfaces.rest.resources.ExtendedGroupResource;
import com.collabrium.tasks.management.interfaces.rest.resources.MemberResource;
import com.collabrium.tasks.management.interfaces.rest.transform.ExtendedGroupResourceFromDTOAssembler;
import com.collabrium.tasks.management.interfaces.rest.transform.MemberResourceFromDTOAssembler;
import com.collabrium.tasks.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  @GetMapping()
  @Operation(
      summary = "Get members by groupId",
      description = "Fetches all the members of a group."
  )
  public ResponseEntity<List<MemberResource>> getMembersDetailsByGroupId(
      @RequestParam Long groupId
  ) {

    var query =
        new GetMembersDetailsByGroupIdQuery(groupId);

    var membersDetails =
        memberDetailsQueryService.handle(query);

    var resources =
        membersDetails
            .stream()
            .map(MemberResourceFromDTOAssembler::toResourceFromDTO)
            .toList();

    return ResponseEntity.ok(resources);
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

  @GetMapping("/details/{memberId}")
  @Operation(summary = "Get member details by member ID", description = "Fetches the details of a member by their ID.")
  public ResponseEntity<MemberResource> getMemberById(
      @PathVariable Long memberId
  ) {

    var getMemberDetailsByIdQuery = new GetMemberDetailsByIdQuery(memberId);

    var memberDetails = memberDetailsQueryService.handle(getMemberDetailsByIdQuery);

    if (memberDetails.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var memberResource = MemberResourceFromDTOAssembler.toResourceFromDTO(memberDetails.get());

    return ResponseEntity.ok(memberResource);
  }

  @GetMapping("/group")
  @Operation(
      summary = "Get group by member authenticated",
      description = "Retrieve the group associated with the authenticated member"
  )
  public ResponseEntity<ExtendedGroupResource> getGroupByUserId(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var getExtendedGroupByUserIdQuery = new GetExtendedGroupByUserIdQuery(user.userId());

    var extendedGroup = memberDetailsQueryService.handle(getExtendedGroupByUserIdQuery);

    if (extendedGroup.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var extendedGroupResource = ExtendedGroupResourceFromDTOAssembler.toResourceFromDTO(extendedGroup.get());

    return ResponseEntity.ok(extendedGroupResource);

  }
}