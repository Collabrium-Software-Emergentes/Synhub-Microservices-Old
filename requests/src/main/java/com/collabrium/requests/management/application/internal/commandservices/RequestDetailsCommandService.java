package com.collabrium.requests.management.application.internal.commandservices;

import com.collabrium.requests.management.application.internal.assemblers.RequestDetailsDTOFromEntityAssembler;
import com.collabrium.requests.management.application.internal.assemblers.TaskDetailsDTOFromTaskResourceAssembler;
import com.collabrium.requests.management.application.internal.dto.RequestDetailsDTO;
import com.collabrium.requests.management.application.internal.outboundservices.messaging.RequestsEventPublisher;
import com.collabrium.requests.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.requests.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.requests.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.requests.management.domain.exceptions.InvalidRequestException;
import com.collabrium.requests.management.domain.exceptions.TaskNotFoundException;
import com.collabrium.requests.management.domain.exceptions.UserNotFoundException;
import com.collabrium.requests.management.domain.model.aggregates.Request;
import com.collabrium.requests.management.domain.model.commands.CreateRequestCommand;
import com.collabrium.requests.management.domain.model.events.RequestCreatedEvent;
import com.collabrium.requests.management.domain.model.valueobjects.RequestType;
import com.collabrium.requests.management.infrastructure.persistence.jpa.repositories.RequestRepository;
import com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for handling request-related commands.
 * Provides business logic for creating requests and validating
 * user and task associations.
 */
@Service
public class RequestDetailsCommandService {

  private final RequestRepository requestRepository;
  private final IamQueryPort iamQueryPort;
  private final TasksQueryPort tasksQueryPort;
  private final GroupsQueryPort groupsQueryPort;
  private final RequestsEventPublisher requestsEventPublisher;

  public RequestDetailsCommandService(
      RequestRepository requestRepository,
      IamQueryPort iamQueryPort,
      TasksQueryPort tasksQueryPort,
      GroupsQueryPort groupsQueryPort,
      RequestsEventPublisher requestsEventPublisher
  ) {

    this.requestRepository = requestRepository;
    this.iamQueryPort = iamQueryPort;
    this.tasksQueryPort = tasksQueryPort;
    this.groupsQueryPort = groupsQueryPort;
    this.requestsEventPublisher = requestsEventPublisher;
  }

  /**
   * Creates a new request based on the provided command.
   *
   * <p>This method performs comprehensive validation including:
   * <ul>
   *   <li>Command and field validation (description, request type, IDs)</li>
   *   <li>User existence verification</li>
   *   <li>Member profile validation (user must be a member)</li>
   *   <li>Task existence verification</li>
   *   <li>Task ownership validation (a task must belong to the member)</li>
   * </ul>
   * </p>
   *
   * <p>Upon successful validation, a new Request entity is created,
   * persisted, and returned as a DTO with task details.</p>
   *
   * @param command the create request command containing user ID, task ID,
   *                description, and request type
   * @return an Optional containing the RequestDetailsDTO with the created
   *         request information and associated task details
   * @throws InvalidRequestException if the command is null, description is
   *         null/blank, a request type is invalid, or IDs are invalid
   * @throws UserNotFoundException if the user does not exist in the system
   * @throws InvalidRequestException if the user is not a member
   * @throws TaskNotFoundException if the task does not exist
   * @throws InvalidRequestException if the task does not belong to the member
   */
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
      TaskDetailsDTOFromTaskResourceAssembler.toDTO(task);

    var requestDetails =
      RequestDetailsDTOFromEntityAssembler.toDTO(
        savedRequest,
        taskDetails
      );

    publishRequestCreatedEvent(
        user, task, savedRequest
    );

    return Optional.of(requestDetails);
  }

  private void publishRequestCreatedEvent(
      com.collabrium.requests.shared.infrastructure.clients.iam.resources.UserOnlyResource member,
      TaskResource task,
      Request request
  ) {

    var leaderUser =
        getLeaderEmailForGroup(task.groupId());

    if (leaderUser == null) {
      return;
    }

    var event = new RequestCreatedEvent(
        leaderUser,
        member.username(),
        member.name(),
        member.surname(),
        task.title(),
        request.getDescription(),
        request.getRequestType(),
        request.getImageUrl()
    );

    requestsEventPublisher.publishRequestCreated(event);
  }

  private String getLeaderEmailForGroup(Long groupId) {

    var group =
        groupsQueryPort.getGroupOnlyByGroupId(groupId);

    if (group == null || group.leaderId() == null) {
      return null;
    }

    var leader =
        iamQueryPort.getUserOnlyById(group.leaderId());

    if (leader == null) {
      return null;
    }

    return leader.email();
  }

  /**
   * Validates the create request command and its fields.
   *
   * <p>This method performs null checks and delegates field-specific
   * validations to their respective private methods.</p>
   *
   * @param command the create request command to validate
   * @throws InvalidRequestException if the command is null, the description is
   *         invalid, a request type is invalid, or IDs are invalid
   */
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

  /**
   * Validates the request description.
   *
   * <p>The description must not be null, empty, or contain only whitespace.</p>
   *
   * @param description the description text to validate
   * @throws InvalidRequestException if the description is null, empty, or blank
   */
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

  /**
   * Validates the request type string.
   *
   * <p>This method attempts to convert the string to a RequestType enum value.
   * If conversion fails, an exception is thrown.</p>
   *
   * @param requestType the request type string to validate (e.g., "JOIN", "LEAVE")
   * @throws InvalidRequestException if the request type string does not match
   *         any valid RequestType enum value
   */
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

  /**
   * Validates the task ID.
   *
   * <p>The task ID must not be null and must be greater than zero.</p>
   *
   * @param taskId the task ID to validate
   * @throws InvalidRequestException if the task ID is null or less than or equal to zero
   */
  private void validateTaskId(
      Long taskId
  ) {

    if (taskId == null || taskId <= 0) {
      throw InvalidRequestException
          .forInvalidTaskId(taskId);
    }
  }

  /**
   * Validates the user ID.
   *
   * <p>The user ID must not be null and must be greater than zero.</p>
   *
   * @param userId the user ID to validate
   * @throws InvalidRequestException if the user ID is null or less than or equal to zero
   */
  private void validateUserId(
      Long userId
  ) {

    if (userId == null || userId <= 0) {
      throw InvalidRequestException
          .forInvalidUserId(userId);
    }
  }
}