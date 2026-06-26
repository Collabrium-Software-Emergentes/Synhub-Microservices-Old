package com.collabrium.media.media.management.domain.services;

import com.collabrium.media.media.management.domain.model.commands.DeleteGroupImageCommand;
import com.collabrium.media.media.management.domain.model.commands.UploadGroupImageCommand;
import com.collabrium.media.media.management.domain.model.responses.ImageUploadResponse;

import java.util.Optional;

public interface ImageCommandService {

  Optional<ImageUploadResponse> handle(UploadGroupImageCommand command);

  void handle(DeleteGroupImageCommand command);
}