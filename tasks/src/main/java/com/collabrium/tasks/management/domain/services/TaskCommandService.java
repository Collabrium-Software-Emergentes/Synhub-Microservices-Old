package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.commands.DeleteTaskCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteTasksByGroupIdCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteTasksByMemberId;

public interface TaskCommandService {

  void handle(DeleteTaskCommand command);

  void handle(DeleteTasksByMemberId command);

  void handle(DeleteTasksByGroupIdCommand command);
}