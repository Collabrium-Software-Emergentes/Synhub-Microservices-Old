package com.collabrium.media.media.management.domain.services;

import com.collabrium.media.media.management.domain.model.commands.DeleteGroupImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UpdateGroupImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UploadGroupImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UploadRequestImageCommand;
import com.collabrium.media.media.management.domain.model.responses.ImageUploadResponse;
import com.collabrium.media.media.management.domain.model.commands.UploadTaskImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UpdateTaskImageCommand;
import com.collabrium.media.media.management.domain.model.commands.DeleteTaskImageCommand;

import java.util.Optional;

public interface ImageCommandService {

  // Groups
  Optional<ImageUploadResponse> handle(UpdateGroupImageCommand command);

  Optional<ImageUploadResponse> handle(UploadGroupImageCommand command);

  Optional<ImageUploadResponse> handle(UploadRequestImageCommand command);

  void handle(DeleteGroupImageCommand command);


  // Tasks
  Optional<ImageUploadResponse> handle(UpdateTaskImageCommand command);

  Optional<ImageUploadResponse> handle(UploadTaskImageCommand command);

  void handle(DeleteTaskImageCommand command);
}