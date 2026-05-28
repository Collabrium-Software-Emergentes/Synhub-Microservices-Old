package com.collabrium.requests.management.domain.exceptions;

public class InvalidRequestTypeException extends RuntimeException {

  public InvalidRequestTypeException(String type) {
    super("Invalid request type: " + type);
  }

  public static InvalidRequestTypeException forNullOrBlank() {
    return new InvalidRequestTypeException(
        "type cannot be null or blank"
    );
  }
}
