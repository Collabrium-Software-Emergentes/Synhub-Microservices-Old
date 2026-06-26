package com.collabrium.media.media.management.domain.model.commands;

public record DeleteGroupImageCommand(
    Long groupId,
    String publicId
) {
}