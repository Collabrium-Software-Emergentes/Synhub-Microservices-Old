package com.collabrium.requests.management.domain.exceptions;

public class TaskNotFoundException extends RuntimeException {

  public TaskNotFoundException(String message) {
    super(message);
  }

  public static TaskNotFoundException forId(
      Long taskId
  ) {

    return new TaskNotFoundException(
        "Task with id " +
            taskId +
            " was not found"
    );
  }
}