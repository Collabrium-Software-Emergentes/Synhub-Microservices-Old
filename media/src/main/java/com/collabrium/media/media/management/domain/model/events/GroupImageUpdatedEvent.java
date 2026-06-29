package com.collabrium.media.media.management.domain.model.events;

public record GroupImageUpdatedEvent(
    Long groupId,
    String imageUrl,
    String publicId
) {
}