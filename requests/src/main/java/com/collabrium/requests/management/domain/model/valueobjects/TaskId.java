package com.collabrium.requests.management.domain.model.valueobjects;

import com.collabrium.requests.management.domain.exceptions.InvalidTaskIdException;
import jakarta.persistence.Embeddable;

@Embeddable
public record TaskId(Long value) {

  public TaskId {

    if (value == null) {
      throw InvalidTaskIdException
          .forNullValue();
    }

    if (value <= 0) {
      throw InvalidTaskIdException
          .forInvalidValue(value);
    }
  }
}