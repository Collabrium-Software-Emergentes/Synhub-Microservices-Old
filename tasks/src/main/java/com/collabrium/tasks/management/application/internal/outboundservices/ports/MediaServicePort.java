package com.collabrium.tasks.management.application.internal.outboundservices.ports;

import com.collabrium.tasks.shared.infrastructure.clients.media.resources.ImageUploadResource;
import org.springframework.web.multipart.MultipartFile;

public interface MediaServicePort {
  ImageUploadResource uploadTaskImage(MultipartFile file);
  ImageUploadResource updateTaskImage(MultipartFile file, Long taskId);
}