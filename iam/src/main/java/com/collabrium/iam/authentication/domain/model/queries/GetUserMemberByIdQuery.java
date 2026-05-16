package com.collabrium.iam.authentication.domain.model.queries;

public record GetUserMemberByIdQuery(
    Long userId,
    Long memberId,
    String authorizationHeader
) {
}
