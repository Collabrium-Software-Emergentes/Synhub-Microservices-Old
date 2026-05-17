package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.commands.CreateTaskCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteTaskCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteTasksByGroupIdCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteTasksByMemberId;
import com.collabrium.tasks.management.domain.model.commands.UpdateTaskCommand;
import com.collabrium.tasks.management.domain.model.commands.UpdateTaskStatusCommand;

import java.util.Optional;

public interface TaskCommandService {

  Optional<Task> handle(CreateTaskCommand command);

  Optional<Task> handle(UpdateTaskCommand command);

  void handle(DeleteTaskCommand command);

  Optional<Task> handle(UpdateTaskStatusCommand command);

  void handle(DeleteTasksByMemberId command);

  void handle(DeleteTasksByGroupIdCommand command);
}