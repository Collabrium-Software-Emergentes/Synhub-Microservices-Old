package com.collabrium.metrics.management.domain.exceptions;

public class GroupNotFoundException extends RuntimeException {

  private GroupNotFoundException(
      String message
  ) {
    super(message);
  }

  public static GroupNotFoundException forLeader(
      Long leaderId
  ) {

    return new GroupNotFoundException(
        "Group not found for leader: "
            + leaderId
    );
  }
}
