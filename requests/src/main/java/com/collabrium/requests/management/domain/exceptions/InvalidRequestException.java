package com.collabrium.requests.management.domain.exceptions;

public class InvalidRequestException extends RuntimeException {

  public InvalidRequestException(String message) {
    super(message);
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
}