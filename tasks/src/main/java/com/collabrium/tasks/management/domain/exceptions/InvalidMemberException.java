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

  public static InvalidMemberException forNullAddGroupCommand() {

    return new InvalidMemberException(
        "AddGroupToMemberCommand cannot be null"
    );
  }

  public static InvalidMemberException forNullRemoveGroupCommand() {

    return new InvalidMemberException(
        "RemoveMemberFromGroupCommand cannot be null"
    );
  }

  public static InvalidMemberException forNullDeleteMembersCommand() {

    return new InvalidMemberException(
        "DeleteMembersByGroupIdCommand cannot be null"
    );
  }

  public static InvalidMemberException forMemberNotFound(Long memberId) {

    return new InvalidMemberException(
        String.format("Member with id %d was not found", memberId)
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

  public static InvalidMemberException forUserIsNotMember(
      Long userId
  ) {

    return new InvalidMemberException(
        "User with id " + userId + " is not a member"
    );
  }

  public static InvalidMemberException forNullLeaveGroupCommand() {

    return new InvalidMemberException(
        "Leave group command cannot be null"
    );
  }

  public static InvalidMemberException forNullUserId() {

    return new InvalidMemberException(
        "User id cannot be null"
    );
  }

  public static InvalidMemberException forMemberWithoutGroup(
      Long memberId
  ) {

    return new InvalidMemberException(
        "Member with id " + memberId + " does not belong to any group"
    );
  }
}
