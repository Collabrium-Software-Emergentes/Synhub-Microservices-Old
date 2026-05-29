package com.collabrium.requests.management.application.internal.queryservices;

import com.collabrium.requests.management.application.internal.dto.RequestDetailsDTO;
import com.collabrium.requests.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.requests.management.application.internal.dto.TaskMemberDetailsDTO;
import com.collabrium.requests.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.requests.management.domain.exceptions.InvalidRequestException;
import com.collabrium.requests.management.domain.exceptions.TaskNotFoundException;
import com.collabrium.requests.management.domain.model.queries.GetRequestDetailsByIdQuery;
import com.collabrium.requests.management.domain.model.queries.GetRequestsDetailsByTaskIdQuery;
import com.collabrium.requests.management.domain.model.valueobjects.TaskId;
import com.collabrium.requests.management.infrastructure.persistence.jpa.repositories.RequestRepository;
import com.collabrium.requests.shared.infrastructure.clients.tasks.resources.TaskResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestDetailsQueryService {

  private final RequestRepository requestRepository;
  private final TasksQueryPort tasksQueryPort;

  public RequestDetailsQueryService(
      RequestRepository requestRepository,
      TasksQueryPort tasksQueryPort
  ) {

    this.requestRepository = requestRepository;
    this.tasksQueryPort = tasksQueryPort;
  }

  public List<RequestDetailsDTO> handle(
      GetRequestsDetailsByTaskIdQuery query
  ) {

    validateQuery(query);

    var task =
        tasksQueryPort.getTaskDetailsById(
            query.taskId()
        );

    if (task == null) {
      throw TaskNotFoundException.forId(
          query.taskId()
      );
    }

    var requests =
        requestRepository.findAllByTaskId(
            new TaskId(query.taskId())
        );

    var taskDetails =
        mapToTaskDetailsDTO(task);

    return requests.stream()
        .map(request ->
            new RequestDetailsDTO(
                request.getId(),
                request.getDescription(),
                request.getRequestType(),
                request.getRequestStatus(),
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
        tasksQueryPort.getTaskDetailsById(
            query.taskId()
        );

    if (task == null) {
      throw TaskNotFoundException.forId(
          query.taskId()
      );
    }

    var taskDetails =
        mapToTaskDetailsDTO(task);

    var requestDetails =
        new RequestDetailsDTO(
            request.getId(),
            request.getDescription(),
            request.getRequestType(),
            request.getRequestStatus(),
            taskDetails
        );

    return Optional.of(requestDetails);
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
}