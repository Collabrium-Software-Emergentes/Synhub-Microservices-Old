package com.collabrium.media.media.management.domain.services;

import com.collabrium.media.media.management.domain.model.commands.DeleteGroupImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UpdateGroupImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UploadGroupImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UploadRequestImageCommand;
import com.collabrium.media.media.management.domain.model.responses.ImageUploadResponse;

import java.util.Optional;

public interface ImageCommandService {

  Optional<ImageUploadResponse> handle(UpdateGroupImageCommand command);

  Optional<ImageUploadResponse> handle(UploadGroupImageCommand command);

  Optional<ImageUploadResponse> handle(UploadRequestImageCommand command);

  void handle(DeleteGroupImageCommand command);
}