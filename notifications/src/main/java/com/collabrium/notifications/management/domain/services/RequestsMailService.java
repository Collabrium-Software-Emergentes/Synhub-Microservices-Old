package com.collabrium.notifications.management.domain.services;

import com.collabrium.notifications.management.domain.model.commands.SendRequestCreatedEmailCommand;

public interface RequestsMailService {

  void handle(SendRequestCreatedEmailCommand command);
}
