package com.collabrium.requests.management.interfaces.rest.resources;

public record RequestDetailsResource(
    Long id,
    String description,
    String requestType,
    String requestStatus,
    String imageUrl,
    TaskResource task
) {
}