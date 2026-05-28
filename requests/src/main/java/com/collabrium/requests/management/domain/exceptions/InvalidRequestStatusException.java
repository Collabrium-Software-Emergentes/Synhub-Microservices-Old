package com.collabrium.requests.management.domain.exceptions;

public class InvalidRequestStatusException extends RuntimeException {

  public InvalidRequestStatusException(String status) {
    super("Invalid request status: " + status);
  }

  public static InvalidRequestStatusException forNullOrBlank() {
    return new InvalidRequestStatusException(
        "status cannot be null or blank"
    );
  }
}
