package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.domain.model.commands.LeaveGroupCommand;
import com.collabrium.tasks.management.domain.model.queries.GetAllMembersByGroupIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetMemberByIdQuery;
import com.collabrium.tasks.management.domain.services.MemberCommandService;
import com.collabrium.tasks.management.domain.services.MemberQueryService;
import com.collabrium.tasks.management.interfaces.rest.resources.MemberOnlyResource;
import com.collabrium.tasks.management.interfaces.rest.transform.MemberOnlyResourceFromEntityAssembler;
import com.collabrium.tasks.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
@Tag(name = "Member", description = "Member management API")
public class MemberController {

  private final MemberQueryService memberQueryService;
  private final MemberCommandService memberCommandService;

  public MemberController(
      MemberQueryService memberQueryService,
      MemberCommandService memberCommandService
  ) {

    this.memberQueryService = memberQueryService;
    this.memberCommandService = memberCommandService;
  }

  @GetMapping("{memberId}")
  @Operation(summary = "Get member details by id", description = "Fetches the details of the member.")
  public ResponseEntity<MemberOnlyResource> getMemberById(
      @PathVariable Long memberId
  ) {

    var getMemberByIdQuery = new GetMemberByIdQuery(memberId);
    var memberOptional = memberQueryService.handle(getMemberByIdQuery);
    if (memberOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var memberResource = MemberOnlyResourceFromEntityAssembler.toResourceFromEntity(memberOptional.get());
    return ResponseEntity.ok(memberResource);
  }

  @DeleteMapping("/group/leave")
  @Operation(
      summary = "Leave group by member authenticated",
      description = "Allows the authenticated member to leave their group.")
  public ResponseEntity<Void> leaveGroupByMemberAuthenticated(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var leaveGroupCommand = new LeaveGroupCommand(user.userId());

    memberCommandService.handle(leaveGroupCommand);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/basic")
  @Operation(
      summary = "Get all members by group ID",
      description = "Fetches a list of all members belonging to the specified group."
  )
  public ResponseEntity<List<MemberOnlyResource>> getAllMembersByGroupId(
      @RequestParam Long groupId
  ) {

    var getAllMembersByGroupIdQuery = new GetAllMembersByGroupIdQuery(groupId);

    var members = memberQueryService.handle(getAllMembersByGroupIdQuery);

    var membersResources = members
        .stream()
        .map(MemberOnlyResourceFromEntityAssembler::toResourceFromEntity)
        .toList();

    return ResponseEntity.ok(membersResources);
  }
}
