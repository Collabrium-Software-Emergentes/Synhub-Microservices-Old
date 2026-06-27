package com.collabrium.media.media.management.interfaces.rest.controllers;

import com.collabrium.media.media.management.domain.model.commands.UploadGroupImageCommand;
import com.collabrium.media.media.management.domain.services.ImageCommandService;
import com.collabrium.media.media.management.interfaces.rest.resources.ImageUploadResource;
import com.collabrium.media.media.management.interfaces.rest.transform.ImageUploadResourceFromResponseAssembler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(
    value = "/api/v1/images",
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class ImageController {

  private final ImageCommandService imageCommandService;

  public ImageController(
      ImageCommandService imageCommandService
  ) {

    this.imageCommandService = imageCommandService;
  }

  @PutMapping(
      value = "/groups/{groupId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<ImageUploadResource> updateGroupImage(
      @PathVariable Long groupId,
      @RequestParam("file") MultipartFile file
  ) {

    var command = new UploadGroupImageCommand(groupId, file);

    var response = imageCommandService.handle(command);

    if (response.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var resource =
        ImageUploadResourceFromResponseAssembler
            .toResourceFromResponse(response.get());

    return ResponseEntity.ok(resource);
  }
}