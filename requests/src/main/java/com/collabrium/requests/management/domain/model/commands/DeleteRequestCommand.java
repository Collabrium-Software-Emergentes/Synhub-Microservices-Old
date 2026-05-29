package com.collabrium.requests.management.domain.model.commands;

public record DeleteRequestCommand(
    Long taskId,
    Long requestId
) {
}