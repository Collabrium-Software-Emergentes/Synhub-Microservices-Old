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
  private final TasksQueryPort tasksQueryPort;

  public ImageCommandServiceImpl(
          Cloudinary cloudinary,
          GroupsQueryPort groupsQueryPort,
          TasksQueryPort tasksQueryPort
  ) {
    this.cloudinary = cloudinary;
    this.groupsQueryPort = groupsQueryPort;
    this.tasksQueryPort = tasksQueryPort;
  }


  @Override
  public Optional<ImageUploadResponse> handle(UpdateGroupImageCommand command) {

    var group = groupsQueryPort.getGroupById(command.groupId());

    if (group == null) {
      throw new RuntimeException("Group not found");
    }

    try {

      if (group.publicId() != null &&
          !group.publicId().isBlank()) {

        cloudinary.uploader().destroy(
            group.publicId(),
            ObjectUtils.asMap(
                "resource_type", "image"
            )
        );
      }

      Map<String, Object> result =
          cloudinary.uploader().upload(
              command.file().getBytes(),
              ObjectUtils.asMap(
                  "folder", "synhub/groups",
                  "public_id", UUID.randomUUID().toString(),
                  "resource_type", "image"
              )
          );

      return Optional.of(mapResponse(result));

    } catch (Exception e) {
      throw new RuntimeException(e);
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

    try {

      cloudinary.uploader().destroy(
          command.publicId(),
          ObjectUtils.emptyMap()
      );

    } catch (Exception e) {

      throw new RuntimeException(
          "Error deleting image: " + e.getMessage(),
          e
      );
    }

  }

  @Override
  public Optional<ImageUploadResponse> handle(UpdateTaskImageCommand command) {

    var task = tasksQueryPort.getTaskById(command.taskId());

    if (task == null) {
      throw new RuntimeException("Task not found");
    }

    try {

      if (task.publicId() != null && !task.publicId().isBlank()) {
        cloudinary.uploader().destroy(
                task.publicId(),
                ObjectUtils.asMap("resource_type", "image")
        );
      }

      Map<String, Object> result = cloudinary.uploader().upload(
              command.file().getBytes(),
              ObjectUtils.asMap(
                      "folder", "synhub/tasks",
                      "public_id", UUID.randomUUID().toString(),
                      "resource_type", "image"
              )
      );

      return Optional.of(mapResponse(result));

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<ImageUploadResponse> handle(UploadTaskImageCommand command) {

    try {

      Map<String, Object> result = cloudinary.uploader().upload(
              command.file().getBytes(),
              ObjectUtils.asMap(
                      "folder", "synhub/tasks",
                      "public_id", UUID.randomUUID().toString(),
                      "resource_type", "image",
                      "overwrite", false
              )
      );

      return Optional.of(mapResponse(result));

    } catch (Exception e) {
      throw new RuntimeException("Error uploading image: " + e.getMessage(), e);
    }
  }

  @Override
  public void handle(DeleteTaskImageCommand command) {

    try {
      cloudinary.uploader().destroy(command.publicId(), ObjectUtils.emptyMap());
    } catch (Exception e) {
      throw new RuntimeException("Error deleting image: " + e.getMessage(), e);
    }
  }


  private ImageUploadResponse mapResponse(Map<String, Object> result) {
    return new ImageUploadResponse(
        result.get("secure_url").toString(),
        result.get("public_id").toString()
    );
  }
}