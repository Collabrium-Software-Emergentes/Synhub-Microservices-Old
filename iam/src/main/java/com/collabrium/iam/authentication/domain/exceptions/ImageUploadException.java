package com.collabrium.iam.authentication.domain.exceptions;

public class ImageUploadException extends RuntimeException {

  public ImageUploadException() {
    super("Image upload failed");
  }
}
