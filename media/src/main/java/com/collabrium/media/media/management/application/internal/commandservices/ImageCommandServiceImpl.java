package com.collabrium.media.media.management.application.internal.commandservices;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.collabrium.media.media.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.media.media.management.domain.model.commands.DeleteGroupImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UpdateGroupImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UploadGroupImageCommand;
import com.collabrium.media.media.management.domain.model.responses.ImageUploadResponse;
import com.collabrium.media.media.management.domain.services.ImageCommandService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageCommandServiceImpl implements ImageCommandService {

  private final Cloudinary cloudinary;
  private final GroupsQueryPort groupsQueryPort;

  public ImageCommandServiceImpl(
      Cloudinary cloudinary,
      GroupsQueryPort groupsQueryPort
  ) {

    this.cloudinary = cloudinary;
    this.groupsQueryPort = groupsQueryPort;
  }

  @Override
  public Optional<ImageUploadResponse> handle(UpdateGroupImageCommand command) {

    var group = groupsQueryPort.getGroupById(command.groupId());

    if (group == null) {
      throw new RuntimeException("Group not found");
    }

    try {

      Map<String, Object> result =
          cloudinary.uploader().upload(
              command.file().getBytes(),
              ObjectUtils.asMap(
                  "folder", "synhub/groups",
                  "resource_type", "image",
                  "overwrite", true
              )
          );

      var imageResponse = mapResponse(result);

      return Optional.of(imageResponse);

    } catch (Exception e) {

      throw new RuntimeException(
          "Error uploading image: " + e.getMessage(),
          e
      );
    }
  }

  @Override
  public Optional<ImageUploadResponse> handle(UploadGroupImageCommand command) {

    try {

      Map<String, Object> result =
          cloudinary.uploader().upload(
              command.file().getBytes(),
              ObjectUtils.asMap(
                  "folder", "synhub/groups",
                  "public_id", UUID.randomUUID().toString(),
                  "resource_type", "image",
                  "overwrite", false
              )
          );

      var imageResponse = mapResponse(result);

      return Optional.of(imageResponse);

    } catch (Exception e) {

      throw new RuntimeException(
          "Error uploading image: " + e.getMessage(),
          e
      );
    }
  }

  @Override
  public void handle(DeleteGroupImageCommand command) {

    var group = groupsQueryPort.getGroupById(command.groupId());

    if (group == null) {
      throw new RuntimeException("Group not found");
    }

    try {

      cloudinary.uploader().destroy(
          group.publicId(),
          ObjectUtils.emptyMap()
      );

    } catch (Exception e) {

      throw new RuntimeException(
          "Error deleting image: " + e.getMessage(),
          e
      );
    }

  }

  private ImageUploadResponse mapResponse(Map<String, Object> result) {
    return new ImageUploadResponse(
        result.get("secure_url").toString(),
        result.get("public_id").toString()
    );
  }
}