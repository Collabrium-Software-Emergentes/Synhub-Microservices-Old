package com.collabrium.notifications.management.domain.services;

import com.collabrium.notifications.management.domain.model.commands.SendGroupCreatedEmailCommand;

public interface GroupsMailService {

  void handle(SendGroupCreatedEmailCommand command);
}