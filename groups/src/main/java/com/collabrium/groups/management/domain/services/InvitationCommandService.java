package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.commands.AcceptInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.CancelInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.ProcessInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.RejectInvitationCommand;

public interface InvitationCommandService {

  void handle(RejectInvitationCommand command);

  void handle(CancelInvitationCommand command);

  void handle(AcceptInvitationCommand command);

  void handle(ProcessInvitationCommand command);
}
