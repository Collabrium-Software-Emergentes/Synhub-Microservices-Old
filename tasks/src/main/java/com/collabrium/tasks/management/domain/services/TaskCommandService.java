package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.commands.DeleteTaskCommand;

public interface TaskCommandService {

  void handle(DeleteTaskCommand command);
}