package com.collabrium.requests.management.application.internal.commandservices;

import com.collabrium.requests.management.application.internal.dto.RequestDetailsDTO;
import com.collabrium.requests.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.requests.management.application.internal.dto.TaskMemberDetailsDTO;
import com.collabrium.requests.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.requests.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.requests.management.domain.exceptions.InvalidRequestException;
import com.collabrium.requests.management.domain.exceptions.TaskNotFoundException;
import com.collabrium.requests.management.domain.exceptions.UserNotFoundException;
import com.collabrium.requests.management.domain.model.aggregates.Request;
import com.collabrium.requests.management.domain.model.commands.CreateRequestCommand;
import com.collabrium.requests.management.domain.model.valueobjects.RequestType;
import com.collabrium.requests.management.infrastructure.persistence.jpa.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestDetailsCommandService {

  private final RequestRepository requestRepository;
  private final IamQueryPort iamQueryPort;
  private final TasksQueryPort tasksQueryPort;

  public RequestDetailsCommandService(
      RequestRepository requestRepository,
      IamQueryPort iamQueryPort,
      TasksQueryPort tasksQueryPort
  ) {

    this.requestRepository = requestRepository;
    this.iamQueryPort = iamQueryPort;
    this.tasksQueryPort = tasksQueryPort;
  }

  public Optional<RequestDetailsDTO> handle(
      CreateRequestCommand command
  ) {

    validateCreateCommand(command);

    var user =
        iamQueryPort.getUserOnlyById(
            command.userId()
        );

    if (user == null) {
      throw UserNotFoundException.forId(
          command.userId()
      );
    }

    if (user.memberId() == null) {
      throw InvalidRequestException
          .forUserIsNotMember(
              command.userId()
          );
    }

    var task =
        tasksQueryPort.getTaskDetailsById(
            command.taskId()
        );

    if (task == null) {
      throw TaskNotFoundException.forId(
          command.taskId()
      );
    }

    if (
        task.member() == null ||
            !task.member().id().equals(
                user.memberId()
            )
    ) {

      throw InvalidRequestException
          .forTaskDoesNotBelongToMember(
              command.taskId(),
              user.memberId()
          );
    }

    var request =
        new Request(command);

    var savedRequest =
        requestRepository.save(request);

    var taskDetails =
        mapToTaskDetailsDTO(task);

    var requestDetails =
        new RequestDetailsDTO(
            savedRequest.getId(),
            savedRequest.getDescription(),
            savedRequest.getRequestType(),
            savedRequest.getRequestStatus(),
            taskDetails
        );

    return Optional.of(requestDetails);
  }

  private TaskDetailsDTO mapToTaskDetailsDTO(
      com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource task
  ) {

    var memberDetails =
        new TaskMemberDetailsDTO(
            task.member().id(),
            task.member().name(),
            task.member().surname(),
            task.member().urlImage()
        );

    return new TaskDetailsDTO(
        task.id(),
        task.title(),
        task.description(),
        task.dueDate(),
        task.createdAt(),
        task.updatedAt(),
        task.status(),
        memberDetails,
        task.groupId()
    );
  }

  private void validateCreateCommand(
      CreateRequestCommand command
  ) {

    if (command == null) {
      throw InvalidRequestException
          .forNullCreateCommand();
    }

    validateDescription(
        command.description()
    );

    validateRequestType(
        command.requestType()
    );

    validateTaskId(
        command.taskId()
    );

    validateUserId(
        command.userId()
    );
  }

  private void validateDescription(
      String description
  ) {

    if (description == null) {
      throw InvalidRequestException
          .forNullDescription();
    }

    if (description.isBlank()) {
      throw InvalidRequestException
          .forEmptyDescription();
    }
  }

  private void validateRequestType(
      String requestType
  ) {

    try {

      RequestType.fromString(requestType);

    } catch (Exception exception) {

      throw InvalidRequestException
          .forInvalidRequestType(
              requestType
          );
    }
  }

  private void validateTaskId(
      Long taskId
  ) {

    if (taskId == null || taskId <= 0) {
      throw InvalidRequestException
          .forInvalidTaskId(taskId);
    }
  }

  private void validateUserId(
      Long userId
  ) {

    if (userId == null || userId <= 0) {
      throw InvalidRequestException
          .forInvalidUserId(userId);
    }
  }
}