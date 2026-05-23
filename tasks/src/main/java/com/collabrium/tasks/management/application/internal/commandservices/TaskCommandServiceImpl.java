package com.collabrium.tasks.management.application.internal.commandservices;

import com.collabrium.tasks.management.domain.exceptions.InvalidTaskException;
import com.collabrium.tasks.management.domain.model.commands.DeleteTaskCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteTasksByGroupIdCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteTasksByMemberId;
import com.collabrium.tasks.management.domain.model.valueobjects.GroupId;
import com.collabrium.tasks.management.domain.services.TaskCommandService;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskCommandServiceImpl implements TaskCommandService {

  private final TaskRepository taskRepository;

  public TaskCommandServiceImpl(
      TaskRepository taskRepository
  ) {

    this.taskRepository = taskRepository;
  }

  @Override
  public void handle(DeleteTaskCommand command) {

    if (command == null) {
      throw InvalidTaskException.forNullDeleteCommand();
    }

    var taskId = command.taskId();

    if (!taskRepository.existsById(taskId)) {
      throw InvalidTaskException.forTaskNotFound(taskId);
    }

    taskRepository.deleteById(taskId);
  }

  @Override
  public void handle(DeleteTasksByMemberId command) {

    if (command == null) {
      throw InvalidTaskException.forNullDeleteByMemberCommand();
    }

    taskRepository.deleteAllByMember_Id(
        command.memberId()
    );
  }

  @Override
  public void handle(DeleteTasksByGroupIdCommand command) {

    if (command == null) {
      throw InvalidTaskException.forNullDeleteByGroupCommand();
    }

    taskRepository.deleteAllByGroupId(
        new GroupId(command.groupId())
    );
  }
}