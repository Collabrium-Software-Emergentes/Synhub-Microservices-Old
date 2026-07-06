package com.collabrium.iam.authentication.domain.exceptions;

public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException() {
    super("Invalid verification token");
  }
}
