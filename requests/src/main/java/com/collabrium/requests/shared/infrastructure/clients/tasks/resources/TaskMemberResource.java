package com.collabrium.requests.shared.infrastructure.clients.tasks.resources;

public record TaskMemberResource(
    Long id,
    String name,
    String surname,
    String urlImage
) {
}