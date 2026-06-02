package com.collabrium.requests.management.domain.model.valueobjects;

import com.collabrium.requests.management.domain.exceptions.InvalidRequestStatusException;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RequestStatus {

  PENDING,
  APPROVED,
  REJECTED;

  private static final Map<String, RequestStatus> VALUES =
      Stream.of(values())
          .collect(Collectors.toMap(
              value -> value.name().toLowerCase(),
              value -> value
          ));

  public static RequestStatus fromString(String status) {

    if (status == null || status.isBlank()) {
      throw InvalidRequestStatusException
          .forNullOrBlank();
    }

    var requestStatus =
        VALUES.get(status.toLowerCase());

    if (requestStatus == null) {
      throw new InvalidRequestStatusException(status);
    }

    return requestStatus;
  }
}