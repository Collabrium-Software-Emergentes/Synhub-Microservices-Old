package com.collabrium.tasks.management.domain.exceptions;

public class InvalidTaskException extends RuntimeException {

  private InvalidTaskException(String message) {
    super(message);
  }

  // =========================================================
  // CREATE TASK
  // =========================================================

  public static InvalidTaskException forNullCreateCommand() {
    return new InvalidTaskException(
        "CreateTaskCommand cannot be null"
    );
  }

  public static InvalidTaskException forNullTitle() {
    return new InvalidTaskException(
        "Task title cannot be null"
    );
  }

  public static InvalidTaskException forEmptyTitle() {
    return new InvalidTaskException(
        "Task title cannot be empty"
    );
  }

  public static InvalidTaskException forNullDescription() {
    return new InvalidTaskException(
        "Task description cannot be null"
    );
  }

  public static InvalidTaskException forEmptyDescription() {
    return new InvalidTaskException(
        "Task description cannot be empty"
    );
  }

  public static InvalidTaskException forNullDueDate() {
    return new InvalidTaskException(
        "Task due date cannot be null"
    );
  }

  // =========================================================
  // UPDATE TASK
  // =========================================================

  public static InvalidTaskException forNullUpdateCommand() {
    return new InvalidTaskException(
        "UpdateTaskCommand cannot be null"
    );
  }

  // =========================================================
  // UPDATE STATUS
  // =========================================================

  public static InvalidTaskException forNullUpdateStatusCommand() {
    return new InvalidTaskException(
        "UpdateTaskStatusCommand cannot be null"
    );
  }

  public static InvalidTaskException forNullStatus() {
    return new InvalidTaskException(
        "Task status cannot be null"
    );
  }

  public static InvalidTaskException forInvalidStatus(String status) {
    return new InvalidTaskException(
        String.format(
            "Invalid task status: '%s'",
            status
        )
    );
  }

  // =========================================================
  // MEMBER
  // =========================================================

  public static InvalidTaskException forNullMember() {
    return new InvalidTaskException(
        "Task member cannot be null"
    );
  }

  // =========================================================
  // GROUP
  // =========================================================

  public static InvalidTaskException forNullGroupId() {
    return new InvalidTaskException(
        "Group ID cannot be null"
    );
  }
}
