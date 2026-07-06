package com.collabrium.tasks.shared.infrastructure.clients.media;

import com.collabrium.tasks.shared.infrastructure.clients.media.resources.ImageUploadResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "media-service")
public interface MediaFeignClient {

  @PostMapping(
          value = "/api/v1/images/tasks",
          consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  ImageUploadResource uploadTaskImage(
          @RequestPart("file") MultipartFile file
  );

  @PutMapping(
          value = "/api/v1/images/tasks/{taskId}",
          consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  ImageUploadResource updateTaskImage(
          @PathVariable Long taskId,
          @RequestPart("file") MultipartFile file
  );
}