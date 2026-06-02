package com.collabrium.requests.management.domain.model.valueobjects;

import com.collabrium.requests.management.domain.exceptions.InvalidRequestTypeException;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RequestType {

  SUBMISSION,
  MODIFICATION,
  EXPIRED;

  private static final Map<String, RequestType> VALUES =
      Stream.of(values())
          .collect(Collectors.toMap(
              value -> value.name().toLowerCase(),
              value -> value
          ));

  public static RequestType fromString(String type) {

    if (type == null || type.isBlank()) {
      throw InvalidRequestTypeException
          .forNullOrBlank();
    }

    var requestType =
        VALUES.get(type.toLowerCase());

    if (requestType == null) {
      throw new InvalidRequestTypeException(type);
    }

    return requestType;
  }
}