package com.collabrium.requests.shared.infrastructure.clients.groups.resources;

public record GroupOnlyResource(
    Long id,
    String name,
    Long leaderId
) {
}