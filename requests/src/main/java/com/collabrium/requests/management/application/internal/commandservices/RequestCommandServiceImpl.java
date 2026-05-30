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

/**
 * Service implementation for handling request command operations.
 * Provides business logic for updating request status and deleting requests,
 * with comprehensive validation and error handling.
 */
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

  /**
   * Updates the status of an existing request.
   *
   * <p>This method performs the following validations:
   * <ul>
   *   <li>Command and field validation (IDs, request status)</li>
   *   <li>Task existence verification</li>
   *   <li>Request existence verification</li>
   *   <li>Request-task association validation</li>
   * </ul>
   * </p>
   *
   * <p>Upon successful validation, the request status is updated and
   * the entity is persisted to the repository.</p>
   *
   * @param command the update request status command containing request ID,
   *                task ID, and the new request status
   * @return an Optional containing the updated Request entity
   * @throws InvalidRequestException if the command is null, IDs are invalid,
   *         request status is invalid, or if a persistence error occurs
   * @throws TaskNotFoundException if the specified task does not exist
   * @throws InvalidRequestException if the request is not found or does not
   *         belong to the specified task
   */
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

  /**
   * Deletes an existing request.
   *
   * <p>This method performs the following validations:
   * <ul>
   *   <li>Command and field validation (IDs)</li>
   *   <li>Task existence verification</li>
   *   <li>Request existence verification</li>
   *   <li>Request-task association validation</li>
   * </ul>
   * </p>
   *
   * <p>Upon successful validation, the request is removed from the repository.</p>
   *
   * @param command the delete request command containing request ID and task ID
   * @throws InvalidRequestException if the command is null, IDs are invalid,
   *         or if a deletion error occurs
   * @throws TaskNotFoundException if the specified task does not exist
   * @throws InvalidRequestException if the request is not found or does not
   *         belong to the specified task
   */
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

  /**
   * Retrieves and validates a request by ID and task association.
   *
   * <p>This method first checks if the request exists, then validates
   * that it belongs to the specified task.</p>
   *
   * @param requestId the ID of the request to retrieve
   * @param taskId the ID of the task that the request should belong to
   * @return the validated Request entity
   * @throws InvalidRequestException if the request is not found or does not
   *         belong to the specified task
   */
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

  /**
   * Validates that a request belongs to a specific task.
   *
   * @param request the request entity to validate
   * @param taskId the task ID that the request should be associated with
   * @throws InvalidRequestException if the request's task ID does not match
   *         the provided task ID
   */
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

  /**
   * Validates that a task exists in the system.
   *
   * <p>This method queries the task query port to verify task existence.</p>
   *
   * @param taskId the ID of the task to validate
   * @throws TaskNotFoundException if no task is found with the specified ID
   */
  private void validateTaskExists(
      Long taskId
  ) {

    var task =
        tasksQueryPort.getTaskDetailsById(taskId);

    if (task == null) {
      throw TaskNotFoundException.forId(taskId);
    }
  }

  /**
   * Validates the update request status command.
   *
   * <p>This method performs null checks on the command and delegates
   * field-specific validations.</p>
   *
   * @param command the update request status command to validate
   * @throws InvalidRequestException if the command is null, IDs are invalid,
   *         or the request status is invalid
   */
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

  /**
   * Validates the delete request command.
   *
   * <p>This method performs null checks on the command and delegates
   * ID validations.</p>
   *
   * @param command the delete request command to validate
   * @throws InvalidRequestException if the command is null or IDs are invalid
   */
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

  /**
   * Validates that request and task IDs are valid.
   *
   * <p>Valid IDs must be non-null and greater than zero.</p>
   *
   * @param requestId the request ID to validate
   * @param taskId the task ID to validate
   * @throws InvalidRequestException if either ID is null or less than or equal to zero
   */
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

  /**
   * Validates that the request status string corresponds to a valid enum value.
   *
   * <p>This method attempts to convert the string to a RequestStatus enum value.
   * If conversion fails, an exception is thrown.</p>
   *
   * @param requestStatus the request status string to validate
   * @throws InvalidRequestException if the status string does not match any
   *         valid RequestStatus enum value
   */
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