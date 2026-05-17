package com.collabrium.tasks.management.domain.model.valueobjects;

import com.collabrium.tasks.management.domain.exceptions.InvalidGroupIdException;
import jakarta.persistence.Embeddable;

@Embeddable
public record GroupId(Long value) {

  public GroupId {
    validateGroupId(value);
  }

  private static void validateGroupId(Long value) {

    if (value == null) {
      throw InvalidGroupIdException.forNull();
    }

    if (value <= 0) {
      throw InvalidGroupIdException.forZeroOrNegative(value);
    }
  }
}