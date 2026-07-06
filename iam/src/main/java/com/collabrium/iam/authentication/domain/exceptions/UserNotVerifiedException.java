package com.collabrium.iam.authentication.domain.exceptions;

public class UserNotVerifiedException extends RuntimeException {

  public UserNotVerifiedException(String username) {
    super(String.format("User '%s' is not verified", username));
  }
}
