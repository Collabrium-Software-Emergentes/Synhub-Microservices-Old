package com.collabrium.requests.management.domain.exceptions;

public class InvalidRequestException extends RuntimeException {

  public InvalidRequestException(String message) {
    super(message);
  }

  public static InvalidRequestException forNullCreateCommand() {
    return new InvalidRequestException(
        "Create request command cannot be null"
    );
  }

  public static InvalidRequestException forNullDescription() {
    return new InvalidRequestException(
        "Request description cannot be null"
    );
  }

  public static InvalidRequestException forEmptyDescription() {
    return new InvalidRequestException(
        "Request description cannot be empty"
    );
  }

  public static InvalidRequestException forInvalidRequestType(
      String requestType
  ) {

    return new InvalidRequestException(
        "Invalid request type: " + requestType
    );
  }

  public static InvalidRequestException forInvalidTaskId(
      Long taskId
  ) {

    return new InvalidRequestException(
        "Invalid task id: " + taskId
    );
  }

  public static InvalidRequestException forInvalidUserId(
      Long userId
  ) {

    return new InvalidRequestException(
        "Invalid user id: " + userId
    );
  }

  public static InvalidRequestException forUserIsNotMember(
      Long userId
  ) {

    return new InvalidRequestException(
        "User with id " +
            userId +
            " is not a member"
    );
  }

  public static InvalidRequestException forTaskDoesNotBelongToMember(
      Long taskId,
      Long memberId
  ) {

    return new InvalidRequestException(
        "Task with id " +
            taskId +
            " does not belong to member with id " +
            memberId
    );
  }

  public static InvalidRequestException forInvalidStatusTransition(
      String currentStatus,
      String newStatus
  ) {

    return new InvalidRequestException(
        "Cannot change request status from " +
            currentStatus +
            " to " +
            newStatus
    );
  }

  public static InvalidRequestException forNullGetRequestsByTaskIdQuery() {

    return new InvalidRequestException(
        "Get requests by task id query cannot be null"
    );
  }

  public static InvalidRequestException forNullGetRequestByIdQuery() {

    return new InvalidRequestException(
        "Get request by id query cannot be null"
    );
  }

  public static InvalidRequestException forInvalidRequestId(
      Long requestId
  ) {

    return new InvalidRequestException(
        "Invalid request id: " + requestId
    );
  }

  public static InvalidRequestException forRequestNotFound(
      Long requestId
  ) {

    return new InvalidRequestException(
        "Request with id " +
            requestId +
            " was not found"
    );
  }

  public static InvalidRequestException forRequestDoesNotBelongToTask(
      Long requestId,
      Long taskId
  ) {

    return new InvalidRequestException(
        "Request with id " +
            requestId +
            " does not belong to task with id " +
            taskId
    );
  }

  public static InvalidRequestException forNullUpdateStatusCommand() {

    return new InvalidRequestException(
        "Update request status command cannot be null"
    );
  }

  public static InvalidRequestException forInvalidRequestStatus(
      String requestStatus
  ) {

    return new InvalidRequestException(
        "Invalid request status: " +
            requestStatus
    );
  }

  public static InvalidRequestException forRequestUpdateError(
      String error
  ) {

    return new InvalidRequestException(
        "Error while updating request: " +
            error
    );
  }

  public static InvalidRequestException forNullDeleteCommand() {

    return new InvalidRequestException(
        "Delete request command cannot be null"
    );
  }

  public static InvalidRequestException forRequestDeleteError(
      String message
  ) {

    return new InvalidRequestException(
        "Error while deleting request: " + message
    );
  }
}