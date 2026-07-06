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

  public static InvalidTaskException forUserIsNotLeader(
      Long userId
  ) {

    return new InvalidTaskException(
        "User with id " + userId + " is not a leader"
    );
  }

  public static InvalidTaskException forMemberWithoutGroup(
      Long memberId
  ) {

    return new InvalidTaskException(
        "Member with id " + memberId + " does not belong to a group"
    );
  }

  public static InvalidTaskException forGroupNotFound(
      Long groupId
  ) {

    return new InvalidTaskException(
        "Group with id " + groupId + " was not found"
    );
  }

  public static InvalidTaskException forLeaderNotBelongingToGroup(
      Long userId,
      Long groupId
  ) {

    return new InvalidTaskException(
        "Leader user with id " + userId +
            " does not belong to group " + groupId
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

  public static InvalidTaskException forMemberNotFound(
          Long memberId
  ) {

    return new InvalidTaskException(
            "Member with id " + memberId + " was not found"
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

  // =========================================================
  // DELETE TASK
  // =========================================================

  public static InvalidTaskException forNullDeleteCommand() {
    return new InvalidTaskException(
        "DeleteTaskCommand cannot be null"
    );
  }

  public static InvalidTaskException forTaskNotFound(
      Long taskId
  ) {

    return new InvalidTaskException(
        "Task with id " + taskId + " was not found"
    );
  }

  // =========================================================
  // DELETE TASKS BY MEMBER
  // =========================================================

  public static InvalidTaskException forNullDeleteByMemberCommand() {
    return new InvalidTaskException(
        "DeleteTasksByMemberId command cannot be null"
    );
  }

  // =========================================================
  // DELETE TASKS BY GROUP
  // =========================================================

  public static InvalidTaskException forNullDeleteByGroupCommand() {
    return new InvalidTaskException(
        "DeleteTasksByGroupIdCommand cannot be null"
    );
  }

  public static InvalidTaskException forUserWithoutRole(
      Long userId
  ) {

    return new InvalidTaskException(
        "User with id " + userId +
            " is neither a leader nor a member"
    );
  }

  public static InvalidTaskException forLeaderWithoutGroup(
      Long userId
  ) {

    return new InvalidTaskException(
        "Leader with id " + userId +
            " does not belong to any group"
    );
  }

  public static InvalidTaskException forUserNotBelongingToGroup(
      Long userId,
      Long groupId
  ) {

    return new InvalidTaskException(
        "User with id " + userId +
            " does not belong to group with id " +
            groupId + "."
    );
  }

  public static InvalidTaskException forMemberNotBelongingToGroup(
      Long memberId,
      Long groupId
  ) {

    return new InvalidTaskException(
        String.format(
            "Member with id %d does not belong to group %d",
            memberId,
            groupId
        )
    );
  }
}