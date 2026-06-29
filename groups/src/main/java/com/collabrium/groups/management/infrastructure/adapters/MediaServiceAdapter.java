package com.collabrium.groups.management.infrastructure.adapters;

import com.collabrium.groups.management.application.internal.outboundservices.ports.MediaServicePort;
import com.collabrium.groups.shared.infrastructure.clients.media.MediaFeignClient;
import com.collabrium.groups.shared.infrastructure.clients.media.resources.ImageUploadResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MediaServiceAdapter implements MediaServicePort {

  private final MediaFeignClient client;

  public MediaServiceAdapter(
      MediaFeignClient client
  ) {

    this.client = client;
  }

  @Override
  public ImageUploadResource uploadGroupImage(MultipartFile file) {
    return client.uploadGroupImage(file);
  }

  @Override
  public ImageUploadResource updateGroupImage(MultipartFile file, Long groupId) {
    return client.updateGroupImage(groupId, file);
  }
}