package com.collabrium.metrics.management.domain.exceptions;

public class UserNotFoundException extends RuntimeException {

  private UserNotFoundException(
      String message
  ) {
    super(message);
  }

  public static UserNotFoundException forId(
      Long userId
  ) {

    return new UserNotFoundException(
        "User not found with id: "
            + userId
    );
  }
}
