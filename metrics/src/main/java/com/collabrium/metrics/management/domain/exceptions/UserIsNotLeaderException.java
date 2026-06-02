package com.collabrium.metrics.management.domain.exceptions;

public class UserIsNotLeaderException extends RuntimeException {

  private UserIsNotLeaderException(
      String message
  ) {
    super(message);
  }

  public static UserIsNotLeaderException forUser(
      Long userId
  ) {

    return new UserIsNotLeaderException(
        "User is not a leader: "
            + userId
    );
  }
}
