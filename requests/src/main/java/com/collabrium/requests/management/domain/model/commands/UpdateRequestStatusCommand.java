package com.collabrium.requests.management.domain.model.commands;

public record UpdateRequestStatusCommand(
    Long requestId,
    String requestStatus,
    Long taskId
) {
}