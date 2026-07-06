package com.collabrium.requests.management.application.internal.dto;

public record RequestDetailsDTO(
    Long id,
    String description,
    String requestType,
    String requestStatus,
    String imageUrl,
    TaskDetailsDTO task
) {
}