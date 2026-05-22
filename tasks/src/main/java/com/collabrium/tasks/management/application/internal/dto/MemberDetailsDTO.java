package com.collabrium.tasks.management.application.internal.dto;

public record MemberDetailsDTO(
    Long memberId,
    String username,
    String name,
    String surname,
    String imgUrl,
    String email,
    Long groupId
) {
}