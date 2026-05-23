package com.collabrium.tasks.management.application.internal.dto;

public record TaskMemberDTO(
    Long id,
    String name,
    String surname,
    String urlImage
) {
}