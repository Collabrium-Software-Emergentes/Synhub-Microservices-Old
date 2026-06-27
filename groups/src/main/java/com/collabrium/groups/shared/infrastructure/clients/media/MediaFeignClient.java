package com.collabrium.groups.shared.infrastructure.clients.media;

import com.collabrium.groups.shared.infrastructure.clients.media.resources.ImageUploadResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "media-service")
public interface MediaFeignClient {

  @PostMapping("/api/v1/images/groups")
  ImageUploadResource uploadMedia(
      @RequestParam("file") MultipartFile file
  );
}