package com.collabrium.groups.management.interfaces.rest.resources;

import org.springframework.web.multipart.MultipartFile;

public record UpdateGroupResource(
    String name,
    MultipartFile file,
    String description
) {
}