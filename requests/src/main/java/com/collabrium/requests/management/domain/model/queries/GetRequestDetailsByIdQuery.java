package com.collabrium.requests.management.domain.model.queries;

public record GetRequestDetailsByIdQuery(
    Long taskId,
    Long requestId
) {
}