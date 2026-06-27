package com.collabrium.groups.management.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

public record CreateGroupCommand(
    String name,
    MultipartFile file,
    String description,
    Long userId
) {
}