package com.collabrium.tasks.management.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

public record UpdateTaskCommand(
        Long taskId,
        String title,
        String description,
        OffsetDateTime dueDate,
        Long memberId,
        Long userId,
        MultipartFile file
) {
}