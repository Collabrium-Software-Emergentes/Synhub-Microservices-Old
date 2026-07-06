package com.collabrium.tasks.shared.infrastructure.clients.media.resources;

public record ImageUploadResource(
    String imageUrl,
    String publicId
) {
}