package com.collabrium.requests.management.domain.exceptions;

public class InvalidTaskIdException extends RuntimeException {

  public InvalidTaskIdException(String message) {
    super(message);
  }

  public static InvalidTaskIdException forNullValue() {
    return new InvalidTaskIdException(
        "Task ID cannot be null"
    );
  }

  public static InvalidTaskIdException forInvalidValue(Long value) {
    return new InvalidTaskIdException(
        "Task ID must be greater than zero. Received: " + value
    );
  }
}