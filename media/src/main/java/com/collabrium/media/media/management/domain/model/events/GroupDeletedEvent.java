package com.collabrium.media.media.management.domain.model.events;

public record GroupDeletedEvent(
    Long groupId,
    String publicId
) {
}