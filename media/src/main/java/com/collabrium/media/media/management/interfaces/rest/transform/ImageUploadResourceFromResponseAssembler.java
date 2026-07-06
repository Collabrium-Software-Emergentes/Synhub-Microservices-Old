package com.collabrium.media.media.management.interfaces.rest.transform;

import com.collabrium.media.media.management.domain.model.responses.ImageUploadResponse;
import com.collabrium.media.media.management.interfaces.rest.resources.ImageUploadResource;

public class ImageUploadResourceFromResponseAssembler {

  private ImageUploadResourceFromResponseAssembler() {
  }

  public static ImageUploadResource toResourceFromResponse(
      ImageUploadResponse response
  ) {

    return new ImageUploadResource(
        response.imageUrl(),
        response.publicId()
    );
  }
}