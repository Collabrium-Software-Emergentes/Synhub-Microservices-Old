package com.collabrium.tasks.management.interfaces.rest.resources;

public record MemberResource(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl,
    String email,
    Long groupId
) {
}