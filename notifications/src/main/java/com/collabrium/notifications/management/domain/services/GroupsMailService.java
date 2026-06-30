package com.collabrium.notifications.management.domain.services;

import com.collabrium.notifications.management.domain.model.commands.SendGroupCreatedEmailCommand;
import com.collabrium.notifications.management.domain.model.commands.SendInvitationAcceptedEmailCommand;

public interface GroupsMailService {

  void handle(SendGroupCreatedEmailCommand command);

  void handle(SendInvitationAcceptedEmailCommand command);
}