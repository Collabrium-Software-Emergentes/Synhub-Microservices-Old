package com.collabrium.media.media.management.domain.model.events;

public record GroupImageUpdated(
    Long groupId,
    String imageUrl,
    String publicId
) {
}