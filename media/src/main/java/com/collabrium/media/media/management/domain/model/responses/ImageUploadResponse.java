package com.collabrium.media.media.management.domain.model.responses;

public record ImageUploadResponse(
    String imageUrl,
    String publicId
) {
}