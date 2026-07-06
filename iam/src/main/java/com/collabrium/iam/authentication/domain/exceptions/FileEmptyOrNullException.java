package com.collabrium.iam.authentication.domain.exceptions;

public class FileEmptyOrNullException extends RuntimeException {

  public FileEmptyOrNullException() {
    super("File is empty or null");
  }
}
