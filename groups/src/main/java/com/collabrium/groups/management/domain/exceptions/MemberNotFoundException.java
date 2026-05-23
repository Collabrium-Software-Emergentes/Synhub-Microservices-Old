package com.collabrium.groups.management.domain.exceptions;

public class MemberNotFoundException extends RuntimeException {

  public MemberNotFoundException(Long userId) {
    super(
        "User with id " + userId +
            " is not registered as a member"
    );
  }
}
