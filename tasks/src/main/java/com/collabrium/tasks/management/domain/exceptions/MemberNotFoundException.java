package com.collabrium.tasks.management.domain.exceptions;

public class MemberNotFoundException extends RuntimeException {

  public MemberNotFoundException(String message) {
    super(message);
  }

  public static MemberNotFoundException forId(
      Long memberId
  ) {

    return new MemberNotFoundException(
        "Member not found with id: " + memberId
    );
  }

  public static MemberNotFoundException forUser(
      Long userId
  ) {

    return new MemberNotFoundException(
        "User with id " + userId + " is not a member"
    );
  }
}