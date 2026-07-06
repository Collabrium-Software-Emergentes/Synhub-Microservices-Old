package com.collabrium.iam.authentication.domain.exceptions;

public class InvalidPasswordException extends RuntimeException {

  public InvalidPasswordException() {
    super("Invalid password");
  }
}
