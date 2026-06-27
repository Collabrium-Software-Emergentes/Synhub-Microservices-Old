package com.collabrium.groups.shared.infrastructure.clients.media.resources;

public record ImageUploadResource(
    String imageUrl,
    String publicId
) {
}