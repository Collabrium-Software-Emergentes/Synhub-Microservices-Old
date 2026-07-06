package com.collabrium.media.media.management.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

public record UploadRequestImageCommand(
    MultipartFile file
) {
}
