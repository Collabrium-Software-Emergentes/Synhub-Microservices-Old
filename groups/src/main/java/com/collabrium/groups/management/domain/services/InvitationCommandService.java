package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.aggregates.Invitation;
import com.collabrium.groups.management.domain.model.commands.AcceptInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.CancelInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.CreateInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.RejectInvitationCommand;

import java.util.Optional;

public interface InvitationCommandService {

  Optional<Invitation> handle(CreateInvitationCommand command);

  void handle(RejectInvitationCommand command);

  void handle(CancelInvitationCommand command);

  void handle(AcceptInvitationCommand command);
}
