package com.collabrium.requests.management.application.internal.commandservices;

import com.collabrium.requests.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.requests.management.domain.exceptions.InvalidRequestException;
import com.collabrium.requests.management.domain.exceptions.TaskNotFoundException;
import com.collabrium.requests.management.domain.model.aggregates.Request;
import com.collabrium.requests.management.domain.model.commands.UpdateRequestStatusCommand;
import com.collabrium.requests.management.domain.model.valueobjects.RequestStatus;
import com.collabrium.requests.management.domain.services.RequestCommandService;
import com.collabrium.requests.management.infrastructure.persistence.jpa.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestCommandServiceImpl implements RequestCommandService {

  private final RequestRepository requestRepository;
  private final TasksQueryPort tasksQueryPort;

  public RequestCommandServiceImpl(
      RequestRepository requestRepository,
      TasksQueryPort tasksQueryPort
  ) {

    this.requestRepository = requestRepository;
    this.tasksQueryPort = tasksQueryPort;
  }

  @Override
  public Optional<Request> handle(
      UpdateRequestStatusCommand command
  ) {

    validateCommand(command);

    var task =
        tasksQueryPort.getTaskDetailsById(
            command.taskId()
        );

    if (task == null) {
      throw TaskNotFoundException.forId(
          command.taskId()
      );
    }

    var request =
        requestRepository.findById(
            command.requestId()
        ).orElseThrow(() ->
            InvalidRequestException
                .forRequestNotFound(
                    command.requestId()
                )
        );

    if (
        !request.getTaskId().value().equals(
            command.taskId()
        )
    ) {

      throw InvalidRequestException
          .forRequestDoesNotBelongToTask(
              command.requestId(),
              command.taskId()
          );
    }

    request.updateRequestStatus(
        command.requestStatus()
    );

    try {

      var updatedRequest =
          requestRepository.save(request);

      return Optional.of(updatedRequest);

    } catch (Exception exception) {

      throw InvalidRequestException
          .forRequestUpdateError(
              exception.getMessage()
          );
    }
  }

  private void validateCommand(
      UpdateRequestStatusCommand command
  ) {

    if (command == null) {
      throw InvalidRequestException
          .forNullUpdateStatusCommand();
    }

    if (
        command.requestId() == null ||
            command.requestId() <= 0
    ) {

      throw InvalidRequestException
          .forInvalidRequestId(
              command.requestId()
          );
    }

    if (
        command.taskId() == null ||
            command.taskId() <= 0
    ) {

      throw InvalidRequestException
          .forInvalidTaskId(
              command.taskId()
          );
    }

    validateRequestStatus(
        command.requestStatus()
    );
  }

  private void validateRequestStatus(
      String requestStatus
  ) {

    try {

      RequestStatus.fromString(
          requestStatus
      );

    } catch (Exception exception) {

      throw InvalidRequestException
          .forInvalidRequestStatus(
              requestStatus
          );
    }
  }
}