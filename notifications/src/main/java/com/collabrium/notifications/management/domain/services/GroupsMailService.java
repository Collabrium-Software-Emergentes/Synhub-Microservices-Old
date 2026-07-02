package com.collabrium.notifications.management.domain.services;

import com.collabrium.notifications.management.domain.model.commands.*;

public interface GroupsMailService {

  void handle(SendGroupCreatedEmailCommand command);

  void handle(SendInvitationAcceptedEmailCommand command);

  void handle(SendInvitationCreatedEmailCommand command);

  void handle(SendRemoveMemberEmailCommand command);

  void handle(SendGroupDeletedEmailCommand command);
}