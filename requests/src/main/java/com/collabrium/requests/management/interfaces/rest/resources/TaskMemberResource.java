package com.collabrium.requests.management.interfaces.rest.resources;

public record TaskMemberResource(
    Long id,
    String name,
    String surname,
    String urlImage
) {
}