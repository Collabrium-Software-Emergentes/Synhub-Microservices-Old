package com.collabrium.iam.authentication.domain.exceptions;

public class DifferentPasswordException extends RuntimeException {

  public DifferentPasswordException() {
    super("The current password does not match");
  }
}
