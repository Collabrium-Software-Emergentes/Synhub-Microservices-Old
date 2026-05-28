package com.collabrium.requests.management.domain.model.commands;

public record CreateRequestCommand(
    String description,
    String requestType,
    Long taskId
) {
}