package com.collabrium.iam.authentication.domain.exceptions;

public class RoleNotFoundException extends RuntimeException {

  public RoleNotFoundException(String role) {
    super(String.format("Role '%s' was not found", role));
  }
}
