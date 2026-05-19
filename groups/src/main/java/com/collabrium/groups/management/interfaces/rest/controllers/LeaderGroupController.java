package com.collabrium.groups.management.interfaces.rest.controllers;

import com.collabrium.groups.management.domain.services.GroupCommandService;
import com.collabrium.groups.management.interfaces.rest.resources.CreateGroupResource;
import com.collabrium.groups.management.interfaces.rest.resources.GroupResource;
import com.collabrium.groups.management.interfaces.rest.resources.UpdateGroupResource;
import com.collabrium.groups.management.interfaces.rest.transform.CreateGroupCommandFromResourceAssembler;
import com.collabrium.groups.management.interfaces.rest.transform.GroupResourceFromEntityAssembler;
import com.collabrium.groups.management.interfaces.rest.transform.UpdateGroupCommandFromResourceAssembler;
import com.collabrium.groups.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/leader/group")
@Tag(name = "Groups", description = "Group management API")
public class LeaderGroupController {

  private final GroupCommandService groupCommandService;

  public LeaderGroupController(
      GroupCommandService groupCommandService
  ) {

    this.groupCommandService = groupCommandService;
  }

  @PostMapping
  @Operation(summary = "Create a new group", description = "Creates a new group")
  public ResponseEntity<GroupResource> createGroup(
      @RequestBody CreateGroupResource createGroupResource,
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var createGroupCommand = CreateGroupCommandFromResourceAssembler
        .toCommandFromResource(createGroupResource, user.userId());

    var groupOptional = groupCommandService.handle(createGroupCommand);

    if (groupOptional.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var groupResource = GroupResourceFromEntityAssembler
        .toResourceFromEntity(groupOptional.get());

    return ResponseEntity.ok(groupResource);
  }

  @PutMapping
  @Operation(
      summary = "Update a group",
      description = "Updates the authenticated leader group"
  )
  public ResponseEntity<GroupResource> updateGroup(
      @RequestBody UpdateGroupResource updateGroupResource,
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var updateGroupCommand = UpdateGroupCommandFromResourceAssembler
        .toCommandFromResource(user.userId(), updateGroupResource);

    var groupOptional = groupCommandService.handle(updateGroupCommand);

    if (groupOptional.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource = GroupResourceFromEntityAssembler.toResourceFromEntity(groupOptional.get());

    return ResponseEntity.ok(resource);
  }
}
