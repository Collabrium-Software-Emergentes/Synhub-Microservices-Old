package com.collabrium.tasks.management.infrastructure.adapters;

import com.collabrium.tasks.management.application.internal.outboundservices.ports.MediaServicePort;
import com.collabrium.tasks.shared.infrastructure.clients.media.MediaFeignClient;
import com.collabrium.tasks.shared.infrastructure.clients.media.resources.ImageUploadResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MediaServiceAdapter implements MediaServicePort {

    private final MediaFeignClient client;

    public MediaServiceAdapter(
            MediaFeignClient client
    ) {

        this.client = client;
    }

    @Override
    public ImageUploadResource uploadTaskImage(MultipartFile file) {
        return client.uploadTaskImage(file);
    }

    @Override
    public ImageUploadResource updateTaskImage(MultipartFile file, Long taskId) {
        return client.updateTaskImage(taskId, file);
    }
}
