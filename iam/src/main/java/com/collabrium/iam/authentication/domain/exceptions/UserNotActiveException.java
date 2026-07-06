package com.collabrium.iam.authentication.domain.exceptions;

public class UserNotActiveException extends RuntimeException {

  public UserNotActiveException(String username) {
    super(String.format("User '%s' is not active", username));
  }
}
