package com.collabrium.tasks.management.domain.exceptions;

public class InvalidMemberException extends RuntimeException {

  private InvalidMemberException(String message) {
    super(message);
  }

  public static InvalidMemberException forNullCreateCommand() {
    return new InvalidMemberException(
        "CreateMemberCommand cannot be null"
    );
  }

  public static InvalidMemberException forNullGroupId() {
    return new InvalidMemberException(
        "Group ID cannot be null"
    );
  }

  public static InvalidMemberException forNullTask() {
    return new InvalidMemberException(
        "Task cannot be null"
    );
  }
}
