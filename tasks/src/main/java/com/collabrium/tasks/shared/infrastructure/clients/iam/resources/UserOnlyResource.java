package com.collabrium.tasks.shared.infrastructure.clients.iam.resources;

public record UserOnlyResource(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl,
    String email,
    Long leaderId,
    Long memberId
) {
}