package com.collabrium.groups.management.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

public record UpdateGroupCommand(
    Long userId,
    String name,
    String description,
    MultipartFile file
) {
}