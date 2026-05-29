package com.collabrium.requests.management.application.internal.commandservices;

import com.collabrium.requests.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.requests.management.domain.exceptions.InvalidRequestException;
import com.collabrium.requests.management.domain.exceptions.TaskNotFoundException;
import com.collabrium.requests.management.domain.model.aggregates.Request;
import com.collabrium.requests.management.domain.model.commands.DeleteRequestCommand;
import com.collabrium.requests.management.domain.model.commands.UpdateRequestStatusCommand;
import com.collabrium.requests.management.domain.model.valueobjects.RequestStatus;
import com.collabrium.requests.management.domain.services.RequestCommandService;
import com.collabrium.requests.management.infrastructure.persistence.jpa.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestCommandServiceImpl
    implements RequestCommandService {

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

    validateUpdateCommand(command);

    validateTaskExists(command.taskId());

    var request = getValidatedRequest(
        command.requestId(),
        command.taskId()
    );

    request.updateRequestStatus(
        command.requestStatus()
    );

    try {

      return Optional.of(
          requestRepository.save(request)
      );

    } catch (Exception exception) {

      throw InvalidRequestException
          .forRequestUpdateError(
              exception.getMessage()
          );
    }
  }

  @Override
  public void handle(
      DeleteRequestCommand command
  ) {

    validateDeleteCommand(command);

    validateTaskExists(command.taskId());

    var request = getValidatedRequest(
        command.requestId(),
        command.taskId()
    );

    try {

      requestRepository.delete(request);

    } catch (Exception exception) {

      throw InvalidRequestException
          .forRequestDeleteError(
              exception.getMessage()
          );
    }
  }

  private Request getValidatedRequest(
      Long requestId,
      Long taskId
  ) {

    var request =
        requestRepository.findById(requestId)
            .orElseThrow(() ->
                InvalidRequestException
                    .forRequestNotFound(
                        requestId
                    )
            );

    validateRequestBelongsToTask(
        request,
        taskId
    );

    return request;
  }

  private void validateRequestBelongsToTask(
      Request request,
      Long taskId
  ) {

    if (
        !request.getTaskId().value().equals(taskId)
    ) {

      throw InvalidRequestException
          .forRequestDoesNotBelongToTask(
              request.getId(),
              taskId
          );
    }
  }

  private void validateTaskExists(
      Long taskId
  ) {

    var task =
        tasksQueryPort.getTaskDetailsById(taskId);

    if (task == null) {
      throw TaskNotFoundException.forId(taskId);
    }
  }

  private void validateUpdateCommand(
      UpdateRequestStatusCommand command
  ) {

    if (command == null) {
      throw InvalidRequestException
          .forNullUpdateStatusCommand();
    }

    validateIds(
        command.requestId(),
        command.taskId()
    );

    validateRequestStatus(
        command.requestStatus()
    );
  }

  private void validateDeleteCommand(
      DeleteRequestCommand command
  ) {

    if (command == null) {
      throw InvalidRequestException
          .forNullDeleteCommand();
    }

    validateIds(
        command.requestId(),
        command.taskId()
    );
  }

  private void validateIds(
      Long requestId,
      Long taskId
  ) {

    if (requestId == null || requestId <= 0) {

      throw InvalidRequestException
          .forInvalidRequestId(
              requestId
          );
    }

    if (taskId == null || taskId <= 0) {

      throw InvalidRequestException
          .forInvalidTaskId(
              taskId
          );
    }
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