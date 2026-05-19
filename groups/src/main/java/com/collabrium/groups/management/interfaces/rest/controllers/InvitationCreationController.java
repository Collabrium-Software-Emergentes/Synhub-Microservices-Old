package com.collabrium.groups.management.interfaces.rest.controllers;

import com.collabrium.groups.management.application.internal.commandservices.InvitationDetailsCommandService;
import com.collabrium.groups.management.domain.model.commands.CreateInvitationCommand;
import com.collabrium.groups.management.interfaces.rest.resources.InvitationResource;
import com.collabrium.groups.management.interfaces.rest.transform.InvitationResourceFromDTOAssembler;
import com.collabrium.groups.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invitations")
@Tag(name = "Invitations", description = "Invitation Management Endpoints")
public class InvitationCreationController {

  private final InvitationDetailsCommandService invitationDetailsCommandService;

  public InvitationCreationController(
      InvitationDetailsCommandService invitationDetailsCommandService
  ) {

    this.invitationDetailsCommandService = invitationDetailsCommandService;
  }

  @PostMapping("/groups/{groupId}")
  @Operation(summary = "Create a new invitation", description = "Create a new invitation for a group")
  public ResponseEntity<InvitationResource> createInvitation(
      @PathVariable Long groupId,
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var createInvitationCommand = new CreateInvitationCommand(user.userId(), groupId);

    var invitationDetails = invitationDetailsCommandService.handle(createInvitationCommand);

    if (invitationDetails.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var invitationResource = InvitationResourceFromDTOAssembler
        .toResourceFromDTO(invitationDetails.get());

    return ResponseEntity.ok(invitationResource);
  }
}