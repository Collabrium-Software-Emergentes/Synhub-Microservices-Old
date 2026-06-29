package com.collabrium.media.media.management.interfaces.messaging.listeners;

import com.collabrium.media.media.management.domain.model.events.GroupDeletedEvent;
import com.collabrium.media.media.management.domain.services.ImageCommandService;
import com.collabrium.media.media.management.interfaces.messaging.transform.DeleteGroupImageCommandFromEventAssembler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.collabrium.media.media.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.GROUP_DELETED_QUEUE;

@Component
public class GroupsEventListener {

  private final ImageCommandService imageCommandService;

  public GroupsEventListener(
      ImageCommandService imageCommandService
  ) {

    this.imageCommandService = imageCommandService;
  }

  @RabbitListener(queues = GROUP_DELETED_QUEUE)
  public void handle(GroupDeletedEvent event) {

    var deleteGroupImageCommand =
        DeleteGroupImageCommandFromEventAssembler.toCommandFromEvent(event);

    imageCommandService.handle(deleteGroupImageCommand);
  }
}