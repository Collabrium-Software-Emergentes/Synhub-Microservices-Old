package com.collabrium.tasks.management.application.internal.dto;

public record TaskDetailsDTO(
    Long id,
    String title,
    String description,
    String dueDate,
    String createdAt,
    String updatedAt,
    String status,
    Integer timesRearranged,
    Long timePassed,
    TaskMemberDTO member,
    Long groupId,
    String imageUrl
) {
}