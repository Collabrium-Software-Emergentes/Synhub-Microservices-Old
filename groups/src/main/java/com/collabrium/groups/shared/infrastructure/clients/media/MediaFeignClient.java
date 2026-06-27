package com.collabrium.groups.shared.infrastructure.clients.media;

import com.collabrium.groups.shared.infrastructure.clients.media.resources.ImageUploadResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "media-service")
public interface MediaFeignClient {

  @PostMapping(
      value = "/api/v1/images/groups",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  ImageUploadResource uploadMedia(
      @RequestPart("file") MultipartFile file
  );
}