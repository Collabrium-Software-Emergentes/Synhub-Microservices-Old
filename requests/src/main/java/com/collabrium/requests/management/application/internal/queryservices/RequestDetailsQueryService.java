package com.collabrium.requests.management.application.internal.queryservices;

import com.collabrium.requests.management.application.internal.assemblers.RequestDetailsDTOFromEntityAssembler;
import com.collabrium.requests.management.application.internal.assemblers.TaskDetailsDTOFromTaskResourceAssembler;
import com.collabrium.requests.management.application.internal.dto.RequestDetailsDTO;
import com.collabrium.requests.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.requests.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.requests.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.requests.management.domain.exceptions.InvalidRequestException;
import com.collabrium.requests.management.domain.exceptions.TaskNotFoundException;
import com.collabrium.requests.management.domain.exceptions.UserNotFoundException;
import com.collabrium.requests.management.domain.model.queries.GetMyRequestsAsMemberQuery;
import com.collabrium.requests.management.domain.model.queries.GetRequestDetailsByIdQuery;
import com.collabrium.requests.management.domain.model.queries.GetRequestsDetailsByTaskIdQuery;
import com.collabrium.requests.management.domain.model.queries.GetRequestsOfMyGroupAsLeaderQuery;
import com.collabrium.requests.management.domain.model.valueobjects.TaskId;
import com.collabrium.requests.management.infrastructure.persistence.jpa.repositories.RequestRepository;
import com.collabrium.requests.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for handling request query operations.
 * Provides business logic for retrieving request details with various
 *  filters, including by task ID, request ID, member, and leader context.
 */
@Service
public class RequestDetailsQueryService {

  private final RequestRepository requestRepository;
  private final TasksQueryPort tasksQueryPort;
  private final GroupsQueryPort groupsQueryPort;
  private final IamQueryPort iamQueryPort;

  public RequestDetailsQueryService(
    RequestRepository requestRepository,
    TasksQueryPort tasksQueryPort,
    GroupsQueryPort groupsQueryPort,
    IamQueryPort iamQueryPort
  ) {

    this.requestRepository = requestRepository;
    this.tasksQueryPort = tasksQueryPort;
    this.groupsQueryPort = groupsQueryPort;
    this.iamQueryPort = iamQueryPort;
  }

  /**
   * Retrieves all request details for a specific task.
   *
   * <p>This method validates the query, verifies the task exists,
   * fetches all requests associated with the task, and maps them
   * to DTOs with task details.</p>
   *
   * @param query the query containing the task ID
   * @return a list of RequestDetailsDTO for the specified task
   * @throws InvalidRequestException if the query is null or task ID is invalid
   * @throws TaskNotFoundException if the specified task does not exist
   */
  public List<RequestDetailsDTO> handle(
    GetRequestsDetailsByTaskIdQuery query
  ) {

    validateQuery(query);

    var task =
      getTaskOrThrow(
        query.taskId()
      );

    var requests =
      requestRepository.findAllByTaskId(
        new TaskId(query.taskId())
      );

    var taskDetails =
      TaskDetailsDTOFromTaskResourceAssembler.toDTO(task);

    return requests.stream()
      .map(request ->
        RequestDetailsDTOFromEntityAssembler.toDTO(
          request,
          taskDetails
        )
      )
      .toList();
  }

  /**
   * Retrieves detailed information for a specific request by ID.
   *
   * <p>This method validates the query, verifies the request exists,
   * ensures the request belongs to the specified task, and returns
   * the request details enriched with task information.</p>
   *
   * @param query the query containing request ID and task ID
   * @return an Optional containing the RequestDetailsDTO if found
   * @throws InvalidRequestException if the query is null, IDs are invalid,
   *          the request is not found, or the request does not belong to the task
   * @throws TaskNotFoundException if the specified task does not exist
   */
  public Optional<RequestDetailsDTO> handle(
    GetRequestDetailsByIdQuery query
  ) {

    validateQuery(query);

    var request =
      requestRepository.findById(
        query.requestId()
      ).orElseThrow(() ->
        InvalidRequestException
          .forRequestNotFound(
            query.requestId()
          )
      );

    if (
      !request.getTaskId().value().equals(
        query.taskId()
      )
    ) {

      throw InvalidRequestException
        .forRequestDoesNotBelongToTask(
          query.requestId(),
          query.taskId()
        );
    }

    var task =
      getTaskOrThrow(
        query.taskId()
      );

    var taskDetails =
      TaskDetailsDTOFromTaskResourceAssembler.toDTO(task);

    return Optional.of(
      RequestDetailsDTOFromEntityAssembler.toDTO(
        request,
        taskDetails
      )
    );
  }

  /**
   * Retrieves all requests made by a user as a member.
   *
   * <p>This method validates the query, verifies the user exists,
   * confirms the user has a member profile, fetches all tasks
   * assigned to the member, and retrieves all requests associated
   * with those tasks.</p>
   *
   * @param query the query containing the user ID
   * @return a list of RequestDetailsDTO for all tasks of the member
   * @throws InvalidRequestException if the query is null, user ID is invalid,
   *         user is not found, or user is not a member
   */
  public List<RequestDetailsDTO> handle(
    GetMyRequestsAsMemberQuery query
  ) {

    validateQuery(query);

    var user =
      getUserOrThrow(
        query.userId()
      );

    validateMember(user);

    var tasks =
      tasksQueryPort.getTasksDetailsByMemberId(
        user.memberId()
      );

    return getRequestsFromTasks(tasks);
  }

  /**
   * Retrieves all requests from the group where the user is a leader.
   *
   * <p>This method validates the query, verifies the user exists,
   * confirms the user has a leader profile, retrieves the group
   * associated with the leader, fetches all tasks in that group,
   * and retrieves all requests associated with those tasks.</p>
   *
   * @param query the query containing the user ID
   * @return a list of RequestDetailsDTO for all tasks in the leader's group
   * @throws InvalidRequestException if the query is null, user ID is invalid,
   *         user is not found, user is not a leader, or no group is found
   *         for the leader
   */
  public List<RequestDetailsDTO> handle(
    GetRequestsOfMyGroupAsLeaderQuery query
  ) {

    validateQuery(query);

    var user =
      getUserOrThrow(
        query.userId()
      );

    validateLeader(user);

    var group =
      groupsQueryPort.getGroupOnlyByLeaderId(
        user.leaderId()
      );

    if (group == null) {

      throw InvalidRequestException
        .forGroupNotFoundForLeader(
          user.leaderId()
        );
    }

    var tasks =
      tasksQueryPort.getTasksDetailsByGroupId(
        group.id()
      );

    return getRequestsFromTasks(tasks);
  }

  /**
   * Retrieves and maps requests from a list of tasks.
   *
   * <p>This method creates a task map for efficient lookup, fetches
   * all requests associated with the provided task IDs, and maps
   * each request to a DTO containing the corresponding task details.</p>
   *
   * @param tasks the list of TaskResource to extract requests from
   * @return a list of RequestDetailsDTO for all requests in the specified tasks,
   *         or an empty list if no tasks are provided
   */
  private List<RequestDetailsDTO> getRequestsFromTasks(
    List<TaskResource> tasks
  ) {

    if (
      tasks == null ||
        tasks.isEmpty()
    ) {

      return List.of();
    }

    var taskMap =
      tasks.stream()
        .collect(
          Collectors.toMap(
            TaskResource::id,
            TaskDetailsDTOFromTaskResourceAssembler::toDTO
          )
        );

    var taskIds =
      tasks.stream()
        .map(TaskResource::id)
        .toList();

    var requests =
      requestRepository.findAllByTaskIdValueIn(
        taskIds
      );

    return requests.stream()
      .map(request ->
        RequestDetailsDTOFromEntityAssembler.toDTO(
          request,
          taskMap.get(
            request.getTaskId().value()
          )
        )
      )
      .toList();
  }

  /**
   * Retrieves a task by ID or throws an exception if not found.
   *
   * @param taskId the ID of the task to retrieve
   * @return the TaskResource if found
   * @throws TaskNotFoundException if no task exists with the specified ID
   */
  private TaskResource getTaskOrThrow(
    Long taskId
  ) {

    var task =
      tasksQueryPort.getTaskDetailsById(
        taskId
      );

    if (task == null) {
      throw TaskNotFoundException.forId(
        taskId
      );
    }

    return task;
  }

  /**
   * Retrieves a user by ID or throws an exception if not found.
   *
   * @param userId the ID of the user to retrieve
   * @return the UserOnlyResource if found
   * @throws UserNotFoundException if no user exists with the specified ID
   */
  private UserOnlyResource getUserOrThrow(
    Long userId
  ) {

    var user =
      iamQueryPort.getUserOnlyById(
        userId
      );

    if (user == null) {
      throw UserNotFoundException.forId(
        userId
      );
    }

    return user;
  }

  /**
   * Validates that a user has a member profile.
   *
   * @param user the user resource to validate
   * @throws InvalidRequestException if the user's member ID is null
   */
  private void validateMember(
    UserOnlyResource user
  ) {

    if (user.memberId() == null) {

      throw InvalidRequestException
        .forUserIsNotMember(
          user.id()
        );
    }
  }

  /**
   * Validates that a user has a leader profile.
   *
   * @param user the user resource to validate
   * @throws InvalidRequestException if the user's leader ID is null
   */
  private void validateLeader(
    UserOnlyResource user
  ) {

    if (user.leaderId() == null) {

      throw InvalidRequestException
        .forUserIsNotLeader(
          user.id()
        );
    }
  }

  /**
   * Validates that a user ID is valid.
   *
   * <p>A valid user ID must be non-null and greater than zero.</p>
   *
   * @param userId the user ID to validate
   * @throws InvalidRequestException if the user ID is null or less than or equal to zero
   */
  private void validateUserId(
    Long userId
  ) {

    if (
      userId == null ||
        userId <= 0
    ) {

      throw InvalidRequestException
        .forInvalidUserId(
          userId
        );
    }
  }

  /**
   * Validates a GetRequestsDetailsByTaskIdQuery.
   *
   * <p>Ensures the query is not null and contains a valid task ID.</p>
   *
   * @param query the query to validate
   * @throws InvalidRequestException if the query is null or task ID is invalid
   */
  private void validateQuery(
    GetRequestsDetailsByTaskIdQuery query
  ) {

    if (query == null) {

      throw InvalidRequestException
        .forNullGetRequestsByTaskIdQuery();
    }

    if (
      query.taskId() == null ||
        query.taskId() <= 0
    ) {

      throw InvalidRequestException
        .forInvalidTaskId(
          query.taskId()
        );
    }
  }

  /**
   * Validates a GetRequestDetailsByIdQuery.
   *
   * <p>Ensures the query is not null and contains valid task and request IDs.</p>
   *
   * @param query the query to validate
   * @throws InvalidRequestException if the query is null, task ID is invalid,
   *         or request ID is invalid
   */
  private void validateQuery(
    GetRequestDetailsByIdQuery query
  ) {

    if (query == null) {

      throw InvalidRequestException
        .forNullGetRequestByIdQuery();
    }

    if (
      query.taskId() == null ||
        query.taskId() <= 0
    ) {

      throw InvalidRequestException
        .forInvalidTaskId(
          query.taskId()
        );
    }

    if (
      query.requestId() == null ||
        query.requestId() <= 0
    ) {

      throw InvalidRequestException
        .forInvalidRequestId(
          query.requestId()
        );
    }
  }

  /**
   * Validates a GetMyRequestsAsMemberQuery.
   *
   * <p>Ensures the query is not null and contains a valid user ID.</p>
   *
   * @param query the query to validate
   * @throws InvalidRequestException if the query is null or user ID is invalid
   */
  private void validateQuery(
    GetMyRequestsAsMemberQuery query
  ) {

    if (query == null) {

      throw InvalidRequestException
        .forNullGetMyRequestsAsMemberQuery();
    }

    validateUserId(
      query.userId()
    );
  }

  /**
   * Validates a GetRequestsOfMyGroupAsLeaderQuery.
   *
   * <p>Ensures the query is not null and contains a valid user ID.</p>
   *
   * @param query the query to validate
   * @throws InvalidRequestException if the query is null or user ID is invalid
   */
  private void validateQuery(
    GetRequestsOfMyGroupAsLeaderQuery query
  ) {

    if (query == null) {

      throw InvalidRequestException
        .forNullGetRequestsOfMyGroupAsLeaderQuery();
    }

    validateUserId(
      query.userId()
    );
  }
}