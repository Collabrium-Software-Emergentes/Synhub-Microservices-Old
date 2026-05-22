package com.collabrium.groups.management.interfaces.rest.controllers;

import com.collabrium.groups.management.domain.model.commands.CancelInvitationCommand;
import com.collabrium.groups.management.domain.services.InvitationCommandService;
import com.collabrium.groups.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invitations")
@Tag(name = "Invitations", description = "Invitation Management Endpoints")
public class InvitationController {

  private final InvitationCommandService invitationCommandService;

  public InvitationController(
      InvitationCommandService invitationCommandService
  ) {

    this.invitationCommandService = invitationCommandService;
  }

  @DeleteMapping("/member")
  @Operation(summary = "Cancel an invitation", description = "Cancel an existing invitation by a member")
  public ResponseEntity<Void> cancelInvitation(
      @AuthenticationPrincipal AuthenticatedUser user
  ) {

    var cancelInvitationCommand = new CancelInvitationCommand(user.userId());

    invitationCommandService.handle(cancelInvitationCommand);

    return ResponseEntity.noContent().build();
  }
}
