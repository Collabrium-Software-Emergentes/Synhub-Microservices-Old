package com.collabrium.groups.management.application.internal.outboundservices.ports;

import com.collabrium.groups.shared.infrastructure.clients.media.resources.ImageUploadResource;
import org.springframework.web.multipart.MultipartFile;

public interface MediaServicePort {

  ImageUploadResource uploadImage(MultipartFile file);
}