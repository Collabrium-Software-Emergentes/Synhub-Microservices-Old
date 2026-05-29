package com.collabrium.requests.management.interfaces.rest.resources;

public record RequestResource(
    Long id,
    String description,
    String requestType,
    String requestStatus,
    Long taskId
) {
}