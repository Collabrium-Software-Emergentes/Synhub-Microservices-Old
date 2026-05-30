package com.collabrium.requests.management.application.internal.queryservices;

import com.collabrium.requests.management.application.internal.dto.RequestDetailsDTO;
import com.collabrium.requests.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.requests.management.application.internal.dto.TaskMemberDetailsDTO;
import com.collabrium.requests.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.requests.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.requests.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.requests.management.domain.exceptions.InvalidRequestException;
import com.collabrium.requests.management.domain.exceptions.TaskNotFoundException;
import com.collabrium.requests.management.domain.exceptions.UserNotFoundException;
import com.collabrium.requests.management.domain.model.aggregates.Request;
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
      mapToTaskDetailsDTO(task);

    return requests.stream()
      .map(request ->
        mapToRequestDetailsDTO(
          request,
          taskDetails
        )
      )
      .toList();
  }

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
      mapToTaskDetailsDTO(task);

    return Optional.of(
      mapToRequestDetailsDTO(
        request,
        taskDetails
      )
    );
  }

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
            this::mapToTaskDetailsDTO
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
        mapToRequestDetailsDTO(
          request,
          taskMap.get(
            request.getTaskId().value()
          )
        )
      )
      .toList();
  }

  private RequestDetailsDTO mapToRequestDetailsDTO(
    Request request,
    TaskDetailsDTO taskDetails
  ) {

    return new RequestDetailsDTO(
      request.getId(),
      request.getDescription(),
      request.getRequestType(),
      request.getRequestStatus(),
      taskDetails
    );
  }

  private TaskDetailsDTO mapToTaskDetailsDTO(
    TaskResource task
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