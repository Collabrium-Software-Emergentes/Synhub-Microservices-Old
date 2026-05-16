package com.collabrium.iam.authentication.domain.model.queries;

public record GetUserLeaderByIdQuery(
    Long userId,
    Long leaderId
) {
}
