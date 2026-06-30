package com.collabrium.notifications.management.interfaces.messaging.listeners;

import com.collabrium.notifications.management.domain.model.events.GroupCreatedEvent;
import com.collabrium.notifications.management.domain.services.GroupsMailService;
import com.collabrium.notifications.management.interfaces.messaging.transform.SendGroupCreatedEmailCommandFromEventAssembler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.collabrium.notifications.shared.infrastructure.rabbitmq.RabbitMQConfig.GROUP_CREATED_QUEUE;

@Component
public class GroupsEventsListeners {

  private final GroupsMailService groupsMailService;

  public GroupsEventsListeners(
      GroupsMailService groupsMailService
  ) {

    this.groupsMailService = groupsMailService;
  }

  @RabbitListener(queues = GROUP_CREATED_QUEUE)
  public void handle(GroupCreatedEvent event) {

    var sendGroupCreatedEmailCommand =
        SendGroupCreatedEmailCommandFromEventAssembler.toCommandFromEvent(event);

    groupsMailService.handle(sendGroupCreatedEmailCommand);
  }
}